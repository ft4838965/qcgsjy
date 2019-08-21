package com.stylefeng.guns.modular.women.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.async.AsyncTask;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.cache.CacheKit;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.account.service.ISsoAccountService;
import com.stylefeng.guns.modular.man.service.ISsoService;
import com.stylefeng.guns.modular.message.service.IMessageService;
import com.stylefeng.guns.modular.ssoaccoun.service.ISsoAccountFlowService;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.tag.service.ITagService;
import com.stylefeng.guns.modular.vip.service.IVipService;
import com.stylefeng.guns.util.*;
import io.swagger.models.auth.In;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.women.service.ISsoInfoService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 女用户控制器
 *
 * @author fengshuonan
 * @Date 2019-03-01 16:25:53
 */
@Controller
@RequestMapping("/ssoInfo")
public class SsoInfoController extends BaseController {

    private String PREFIX = "/women/ssoInfo/";

    @Autowired
    private ISsoInfoService ssoInfoService;

    @Autowired
    private ISsoAccountService ssoAccountService;

    @Autowired
    private ISsoService ssoService;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private ISsoAccountFlowService accountFlowService;

    @Autowired
    private AsyncTask asyncTask;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IVipService vipService;

    @Autowired
    private Dao dao;

    private ExecutorService executorService = Executors.newFixedThreadPool(50);
    /**
     * 跳转到女用户首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "ssoInfo.html";
    }

    /**
     * 跳转到添加女用户
     */
    @RequestMapping("/ssoInfo_add")
    public String ssoInfoAdd(Model model) {
        model.addAttribute("shengs",dao.selectBySQL("select * from sys_area where pid=0"));
        return PREFIX + "ssoInfo_add.html";
    }

    /**
     * 跳转到修改女用户
     */
    @RequestMapping("/ssoInfo_update/{ssoInfoId}")
    public String ssoInfoUpdate(@PathVariable Integer ssoInfoId, Model model) {

        SsoInfo ssoInfo = ssoInfoService.selectById(ssoInfoId);
        model.addAttribute("item",ssoInfo);

        //回显头像和封面图
        EntityWrapper<Sso> wrapper = new EntityWrapper<>();
        wrapper.eq("sso_id",ssoInfo.getSsoId());
        Sso sso = ssoService.selectOne(wrapper);
        model.addAttribute("avatar",sso.getAvatar());
        model.addAttribute("bigAvatar",sso.getBigAvatar());
        model.addAttribute("checkBigAvatar",sso.getCheckBigAvatar());
        //打赏总额
        Vip vip = vipService.selectOne(new EntityWrapper<Vip>().eq("sso_id", sso.getSsoId()));
        Date now = new Date();
        if (vip == null || vip.getEndTime().getTime() < now.getTime()){
            SsoAccount account = ssoAccountService.selectOne(new EntityWrapper<SsoAccount>().eq("sso_id", sso.getSsoId()));
            model.addAttribute("money",account.getUseableBalance());
        }else {
            List<Map<String, Object>> maps = dao.selectBySQL("SELECT\n" +
                    "\ta.START + b.useable_balance AS startNum \n" +
                    "FROM\n" +
                    "\tt_vip a\n" +
                    "\tLEFT JOIN t_sso_account b ON a.sso_id = b.sso_id \n" +
                    "WHERE\n" +
                    "\ta.end_time > now( ) \n" +
                    "\tAND a.sso_id =" + sso.getSsoId());
            model.addAttribute("money",maps.get(0).get("startNum"));
        }
        //回显标签集合
        List<Integer> tagIds = new ArrayList<>();
        String tags = ssoInfo.getTagIds();
        if (!Tool.isNull(tags)){
            String[] split = tags.split(",");
            for (String tag : split ) {
                String[] split1 = tag.split(":");
                tagIds.add(Integer.parseInt(split1[0]));
            }
        }
        model.addAttribute("multArr",tagIds);

        LogObjectHolder.me().set(ssoInfo);
        return PREFIX + "ssoInfo_edit.html";
    }

    /**
     * 获取女用户列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition,String phone) {
        List<Map<String,Object>> arron = new ArrayList<>();
        String sql = "SELECT\n" +
                "  b.id,\n" +
                "\ta.sso_id as ssoId,\n" +
                "  a.phone,\n" +
                "  a.nick_name as nickName,\n" +
                "  a.create_time as createTime,\n" +
                "  ( CASE a.check_big_avatar WHEN '0' THEN '未审查' WHEN '1' THEN '通过' ELSE '未通过' END ) AS is_check,\n" +
                "  b.birthday,\n" +
                "  b.age,\n" +
                "  b.advantege,\n" +
                "  b.tall,\n" +
                "  b.weight,\n" +
                "  c.useable_balance as total\n" +
                "FROM\n" +
                "\tt_sso a\n" +
                "LEFT JOIN t_sso_info b ON a.sso_id = b.sso_id\n" +
                "LEFT JOIN t_sso_account c ON b.sso_id = c.sso_id\n" +
                "where a.sex = '1' order by c.useable_balance desc";
       if(!Tool.isNull(condition) && Tool.isNull(phone) ){
            sql = sql + "and a.nick_name LIKE " +"'%"+condition + "%'" +" or b.real_name like "+"'%"+condition + "%'";
        }else if(Tool.isNull(condition) && !Tool.isNull(phone) ){
            sql = sql + "and a.phone ="+"'"+phone + "'";
        }else if(!Tool.isNull(condition) && !Tool.isNull(phone) ){
            sql = sql + "and a.nick_name LIKE " +"'%"+condition + "%'"+" or a.phone = "+"'"+phone + "'"+" or b.real_name like "+"'%"+condition + "%'";
        }
        arron = dao.selectBySQL(sql);
        return arron;
    }

    @Autowired
    private ITagService tagService;

    /**
     * 新增女用户
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object add(SsoInfo ssoInfo, Sso sso,String now_sheng, String now_shi, String now_qu) {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("add");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            int sso_id = Integer.parseInt(RandomUtil.getRandom(7));
            //封装账号信息
            sso.setSsoId(sso_id);
            sso.setToken(UuidUtil.get32UUID());
            sso.setSex("1");
            sso.setState("离线");
            sso.setCheckBigAvatar("0");
            sso.setCreateTime(new DateTime());

            //封装基本信息
            ssoInfo.setSsoId(sso_id);
            //封装地址
            ssoInfo.setAreaCode(Integer.parseInt(now_shi));
            List<Map<String, Object>> maps = dao.selectBySQL("SELECT name from sys_area WHERE id in( " + now_sheng + "," + now_shi + "," + now_qu + ")");
            StringBuilder adress = new  StringBuilder();
            if(!Tool.listIsNull(maps)){
                for (Map<String, Object> m: maps) {
                    adress.append(m.get("name"));
                }
            }
            ssoInfo.setAddress(adress+"");
            //封装标签集合
            String tags = ssoInfo.getTagIds();
            String[] tagIds = tags.split(",");
            StringBuffer stringBuffer = new StringBuffer();
            List<Tag> tagList = tagService.selectList(null);
            for (String tagId : tagIds) {
                for (Tag tag:tagList) {
                    if (tag.getId() == Integer.parseInt(tagId)){
                        stringBuffer.append(tagId+":"+tag.getName()+":"+RandomUtil.getRandom(2)+",");
                    }
                }
            }
            ssoInfo.setTagIds(stringBuffer+"");

            //实名认证
/*        String check = CheckIdCard.check(ssoInfo.getIdCard(), ssoInfo.getRealName());
        if(!check.equals("验证成功")){
            return  Erorr_TIP;
        }*/

            //封装账户信息
            SsoAccount ssoAccount = new SsoAccount();
            ssoAccount.setSsoId(sso_id);
            ssoAccount.setStatus("0");
            ssoAccount.setCreateTime(new DateTime());
            ssoAccount.setPayPassword(UuidUtil.get32UUID());
            ssoAccount.setFrozenBalance(0.00);
            ssoAccount.setUseableBalance(0.00);

            //插入数据库
            ssoService.insert(sso);
            ssoInfoService.insert(ssoInfo);
            ssoAccountService.insert(ssoAccount);
        }catch (Exception e){
            e.printStackTrace();
            transactionManager.rollback(status);
            return Erorr_TIP;
        }
        transactionManager.commit(status);
        return SUCCESS_TIP;
    }

    /**
     * 删除女用户
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object delete(@RequestParam Integer ssoInfoId) {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("delete");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            SsoInfo ssoInfo = ssoInfoService.selectById(ssoInfoId);
            //删除基本信息
            ssoInfoService.deleteById(ssoInfoId);
            //删除账号信息
            EntityWrapper<Sso> wrapper1 = new EntityWrapper<>();
            wrapper1.eq("sso_id",ssoInfo.getSsoId());
            ssoService.delete(wrapper1);
            //删除账户信息
            EntityWrapper<SsoAccount> wrapper2 = new EntityWrapper<>();
            wrapper2.eq("sso_id",ssoInfo.getSsoId());
            ssoAccountService.delete(wrapper2);
            //干掉缓存
            Sso sso = ssoService.selectOne(new EntityWrapper<Sso>().eq("sso_id", ssoInfo.getSsoId()));
            if(sso!=null)CacheKit.remove("CONSTANT", sso.getToken());
        }catch (Exception e){
            e.printStackTrace();
            transactionManager.rollback(status);
            return Erorr_TIP;
        }
        transactionManager.commit(status);
        return SUCCESS_TIP;
    }

    /**
     *  结算
     */
    @RequestMapping(value = "/cleanMoney")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object cleanMoney(@RequestParam Integer ssoInfoId) {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("cleanMoney");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            //全部结算
            if (ssoInfoId ==0){
                //异步执行批量任务
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        asyncTask.creatAccountFlow();
                        asyncTask.creatMessage();
                    }
                });
                dao.updateBySQL("UPDATE t_sso_account SET useable_balance=0.0,status='0' ");

            }else{//单个结算
                List<Map<String, Object>> maps = dao.selectBySQL("SELECT a.sso_id,b.useable_balance as b FROM t_sso_info a LEFT JOIN t_sso_account b ON a.sso_id = b.sso_id where a.id="+ssoInfoId);

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(!Tool.listIsNull(maps)){
                            for (Map<String, Object> m : maps) {
                                //创建流水表
                                Double balance = (Double) m.get("b");
                                Integer ssoId = (Integer) m.get("sso_id");
                                SsoAccountFlow accountFlow = new SsoAccountFlow();
                                accountFlow.setMoney(balance);
                                accountFlow.setSsoId(ssoId);
                                accountFlow.setBusinessType("1");
                                accountFlow.setCreateTime(new DateTime());
                                accountFlow.setBusinessName("结算");
                                accountFlow.setComeFrom("0");
                                accountFlowService.insert(accountFlow);
                                System.out.println("执行新建流水单");

                                //创建消息
                                dao.insertBySQL("INSERT INTO t_message ( sso_id, message_sso_id, `type`, content, official_message_type, look, create_time )\n" +
                                        "VALUES\n" +
                                        "\t( 0, "+ssoId+", '0', '您的提现申请已通过，官方已给您结算！', '1', '0', NOW() )");
//                                Message message = new Message();
//                                message.setContent("您的提现申请已通过，官方已给您结算！");
//                                message.setCreateTime(new DateTime());
//                                message.setLook("0");
//                                message.setSsoId(0);
//                                message.setMessageSsoId(ssoId+"");
//                                message.setType("0");
//                                message.setOfficialMessageType("1");
//                                messageService.insert(message);
                            }
                        }
                    }
                });
                dao.updateBySQL("UPDATE t_sso_account a LEFT JOIN t_sso_info b ON a.sso_id = b.sso_id SET useable_balance=0.0,a.status='0' where b.id =" + ssoInfoId);
            }
        }catch (Exception e){
            e.printStackTrace();
            transactionManager.rollback(status);
            return Erorr_TIP;
        }
        transactionManager.commit(status);
        return SUCCESS_TIP;
    }


    /**
     * 修改女用户
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object update(SsoInfo ssoInfo,Sso sso,String money,String origin_money) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("update");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            //封装标签集合
            String tags = ssoInfo.getTagIds();
            String[] tagIds = tags.split(",");
            StringBuffer stringBuffer = new StringBuffer();
            List<Tag> tagList = tagService.selectList(null);
            for (String tagId : tagIds) {
                for (Tag tag:tagList) {
                    if (tag.getId() == Integer.parseInt(tagId)){
                        stringBuffer.append(tagId+":"+tag.getName()+":"+RandomUtil.getRandom(2)+",");
                    }
                }
            }
            ssoInfo.setTagIds(stringBuffer+"");
            dao.updateBySQL("UPDATE t_sso SET avatar = "+"'"+sso.getAvatar()+"'"+",big_avatar = "+"'"+sso.getBigAvatar()+"'"+",check_big_avatar = "+"'"+sso.getCheckBigAvatar()+"'"+" WHERE sso_id = "+ssoInfo.getSsoId());
            if (!money.equals(origin_money)){
                dao.updateBySQL("update t_sso_account set useable_balance="+money+"where sso_id='"+ssoInfo.getSsoId()+"'");

                //创建流水表
                SsoAccountFlow accountFlow = new SsoAccountFlow();
                accountFlow.setMoney(Double.parseDouble(origin_money)-Double.parseDouble(money));
                accountFlow.setSsoId(ssoInfo.getSsoId());
                accountFlow.setBusinessType("1");
                accountFlow.setCreateTime(new DateTime());
                accountFlow.setBusinessName("结算");
                accountFlow.setComeFrom("0");
                accountFlowService.insert(accountFlow);

                //创建消息
                Message message = new Message();
                message.setContent("官方已给您减扣提现 "+ (Double.parseDouble(origin_money)-Double.parseDouble(money))+" 元");
                message.setCreateTime(new Date());
                message.setLook("0");
                message.setSsoId(0);
                message.setMessageSsoId(ssoInfo.getSsoId()+"");
                message.setType("0");
                message.setOfficialMessageType("1");
                messageService.insert(message);
            }

            ssoInfoService.updateById(ssoInfo);
        }catch (Exception e){
            e.printStackTrace();
            transactionManager.rollback(status);
            return Erorr_TIP;
        }
        transactionManager.commit(status);
        return SUCCESS_TIP;
    }

    /**
     * 女用户详情
     */
    @RequestMapping(value = "/detail/{ssoInfoId}")
    @ResponseBody
    public Object detail(@PathVariable("ssoInfoId") Integer ssoInfoId) {
        return ssoInfoService.selectById(ssoInfoId);
    }

}
