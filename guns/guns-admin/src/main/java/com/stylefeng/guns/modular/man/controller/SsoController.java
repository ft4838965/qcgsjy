package com.stylefeng.guns.modular.man.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.account.service.ISsoAccountService;
import com.stylefeng.guns.modular.area.service.IAreaService;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.vip.service.IVipService;
import com.stylefeng.guns.modular.vip.service.impl.VipServiceImpl;
import com.stylefeng.guns.modular.women.service.ISsoInfoService;
import com.stylefeng.guns.util.FSS;
import com.stylefeng.guns.util.RandomUtil;
import com.stylefeng.guns.util.Tool;
import com.stylefeng.guns.util.UuidUtil;
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
import com.stylefeng.guns.modular.man.service.ISsoService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * 男用户控制器
 *
 * @author fengshuonan
 * @Date 2019-03-01 16:24:49
 */
@Controller
@RequestMapping("/sso")
public class SsoController extends BaseController {

    private String PREFIX = "/man/sso/";

    @Autowired
    private ISsoService ssoService;

    @Autowired
    private ISsoInfoService ssoInfoService;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private IVipService vipService;

    @Autowired
    private ISsoAccountService ssoAccountService;

    @Autowired
    private Dao dao;


    /**
     * 跳转到男用户首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "sso.html";
    }

    /**
     * 跳转到添加男用户
     */
    @RequestMapping("/sso_add")
    public String ssoAdd(Model model) {
        model.addAttribute("shengs",dao.selectBySQL("select * from sys_area where pid=0"));
        return PREFIX + "sso_add.html";
    }

    /**
     * 跳转到修改男用户
     */
    @RequestMapping("/sso_update/{ssoId}")
    public String ssoUpdate(@PathVariable Integer ssoId, Model model) {
        Sso sso = ssoService.selectById(ssoId);
        sso.setSex("男");
        model.addAttribute("item",sso);
        EntityWrapper<Vip> wrapper = new EntityWrapper<>();
        wrapper.eq("sso_id",sso.getSsoId());
        Vip vip = vipService.selectOne(wrapper);
        model.addAttribute("start","0");
        model.addAttribute("typeId","0");
        model.addAttribute("isVip","0");
        Date now = new Date();
        if (vip != null && now.getTime()<vip.getEndTime().getTime()){
            model.addAttribute("start",vip.getStart());
            model.addAttribute("typeId",vip.getTypeId());
            model.addAttribute("isVip","1");
        }
        LogObjectHolder.me().set(sso);
        return PREFIX + "sso_edit.html";
    }

    /**
     * 获取所有的用户
     */
    @RequestMapping("/getAll")
    @ResponseBody
    public Object getAll() {
        List<Sso> ssoList = ssoService.selectList(null);
        return ssoList;
    }

    /**
     * 获取男用户列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition,String phone) {
        List<Map<String, Object>> ssoList = new ArrayList<>();
        try{
            String sql = "SELECT\n" +
                    "  a.id,\n" +
                    "\ta.sso_id as ssoId,\n" +
                    "  a.phone,\n" +
                    "  a.nick_name as nickName,\n" +
                    "  a.create_time as createTime,\n" +
                    "  a.token,\n" +
                    "  d.name,\n" +
                    "  c.end_time\n" +
                    "FROM\n" +
                    "\tt_sso a\n" +
                    "LEFT JOIN t_sso_info b ON a.sso_id = b.sso_id\n" +
                    "LEFT JOIN t_vip c ON b.sso_id = c.sso_id\n" +
                    "LEFT JOIN t_vip_type d ON c.type_id = d.id where a.sex = '0' order by a.create_time desc";

            if(!Tool.isNull(condition) && condition.equals("查询所有会员用户")){
                ssoList = dao.selectBySQL(sql);
                Iterator<Map<String, Object> > iterator = ssoList.iterator();
                while(iterator.hasNext()){
                    Map<String, Object> map = iterator.next();
                    String endTime = map.get("end_time")+"";
                    if (!Tool.isNull(endTime)){
                        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = sDateFormat.parse(endTime);
                        Date now = new Date();
                        if (now.getTime()<date.getTime()){
                            map.put("is_vip","是");
                            map.put("vip_type",map.get("name"));
                            map.remove("end_time");
                        }else iterator.remove();
                    }else iterator.remove();
                }
                return ssoList;
            }else if(!Tool.isNull(condition) && Tool.isNull(phone) ){
                   sql = sql + "and a.nick_name LIKE " +"'%"+condition + "%'";
            }else if(Tool.isNull(condition) && !Tool.isNull(phone) ){
                   sql = sql + "and a.phone ="+"'"+phone + "'";
            }else if(!Tool.isNull(condition) && !Tool.isNull(phone) ){
                   sql = sql + "and a.nick_name LIKE " +"'%"+condition + "%'"+" or a.phone = "+"'"+phone + "'";
            }
            ssoList = dao.selectBySQL(sql);
            for (Map<String, Object> map: ssoList) {
                String endTime = map.get("end_time")+"";
                map.put("is_vip","否");
                map.put("vip_type","暂无");
                if (!Tool.isNull(endTime)){
                    SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = sDateFormat.parse(endTime);
                    Date now = new Date();
                    if (now.getTime()<date.getTime()){
                        map.put("is_vip","是");
                        map.put("vip_type",map.get("name"));
                    }
                }
                map.remove("end_time");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return ssoList;
    }

    /**
     * 新增男用户
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object add(Sso sso, Vip vipx, String age, String birthday, String now_sheng, String now_shi, String now_qu,@RequestParam("vip") String isVip) {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("someTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try{
            //封装账号信息
            sso.setSex("0");
            int sso_id = Integer.parseInt(RandomUtil.getRandom(7));
            sso.setSsoId(sso_id);
            sso.setCreateTime(new DateTime());
            sso.setState("离线");
            sso.setToken(UuidUtil.get32UUID());
            sso.setCheckBigAvatar("0");
            //封装基本信息
            SsoInfo ssoInfo = new SsoInfo();
            ssoInfo.setSsoId(sso_id);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ssoInfo.setBirthday(sdf.parse(birthday));
            ssoInfo.setAge(Integer.parseInt(age));
            ssoInfo.setAreaCode(Integer.parseInt(now_shi));
            List<Map<String, Object>> maps = dao.selectBySQL("SELECT name from sys_area WHERE id in( " + now_sheng + "," + now_shi + "," + now_qu + ")");
            StringBuilder adress = new StringBuilder();
            if(!Tool.listIsNull(maps)){
                for (Map<String, Object> m: maps) {
                    adress.append(m.get("name"));
                }
            }
            ssoInfo.setAddress(adress+"");
            ssoInfo.setCreateTime(new DateTime());
            if (isVip.equals("1")){
              vipx.setSsoId(sso_id);
              vipx.setStatus(1);
              vipx.setCreateTime(new DateTime());
              vipx.setValidDate(vipx.getTypeId()*30);
                Date now = new Date();
                Long time = 1 * 2 * 60 * 1000L;//五分钟
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy--MM--dd HH:mm:ss");
                Long end = now.getTime() + time;
                Date date = new Date(end);
                //Date date = calendar.getTime();
                vipx.setEndTime(date);
                vipService.insert(vipx);
            }
            //封装账户信息
            SsoAccount ssoAccount = new SsoAccount();
            ssoAccount.setSsoId(sso_id);
            ssoAccount.setStatus("0");
            ssoAccount.setCreateTime(new DateTime());
            ssoAccount.setPayPassword(UuidUtil.get32UUID());
            ssoAccount.setFrozenBalance(0.00);
            ssoAccount.setUseableBalance(0.00);

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
     * 删除男用户
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object delete(@RequestParam Integer ssoId) {

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("someTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            Sso sso = ssoService.selectById(ssoId);
            ssoService.deleteById(ssoId);
            EntityWrapper<SsoInfo> wrapper = new EntityWrapper<>();
            wrapper.eq("sso_id",sso.getSsoId());
            ssoInfoService.delete(wrapper);
        }catch (Exception e){
            e.printStackTrace();
            transactionManager.rollback(status);
            return Erorr_TIP;
        }
        transactionManager.commit(status);
        return SUCCESS_TIP;
    }

    /**
     * 修改男用户
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Sso sso,Vip vipx,String vip) {
        sso.setSex("0");
        ssoService.updateById(sso);
        if (vip.equals("1")){
            dao.updateBySQL("update t_vip set start = " + vipx.getStart() + ",type_id=" + vipx.getTypeId() + " where sso_id = " + sso.getSsoId());
        }
        return SUCCESS_TIP;
    }

    /**
     * 男用户详情
     */
    @RequestMapping(value = "/detail/{ssoId}")
    @ResponseBody
    public Object detail(@PathVariable("ssoId") Integer ssoId) {
        return ssoService.selectById(ssoId);
    }

    /**
     * 男用户详情
     */
    @RequestMapping("getAreaByPid")
    @ResponseBody
    public Object getAreaByPid(String pid){
        return dao.selectBySQL("select * from sys_area where pid="+pid);
    }


}
