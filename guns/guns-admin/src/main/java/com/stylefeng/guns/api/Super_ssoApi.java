package com.stylefeng.guns.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.cache.CacheKit;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.setting.service.ISettingService;
import com.stylefeng.guns.modular.supersso.service.ISuperSsoService;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.system.model.SuperSso;
import com.stylefeng.guns.modular.system.model.UseableBalance;
import com.stylefeng.guns.modular.useable_balance.service.IUseableBalanceService;
import com.stylefeng.guns.util.DuanXin_LiJun;
import com.stylefeng.guns.util.ResultMsg;
import com.stylefeng.guns.util.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("super_ssoApi")
public class Super_ssoApi {
    @Autowired
    private Dao dao;
    @Autowired
    private ISettingService settingService;
    @Autowired
    private ISuperSsoService superSsoService;
    @Autowired
    private IUseableBalanceService useableBalanceService;
    @RequestMapping("super_login")
    String super_login(Model model, String id, HttpSession session){
        //微信从聊天记录(私聊,群聊)里分享链接到朋友圈没有缩略图简介标题解决方式--李俊
        Tool.putWX_config(model, settingService);
        if (Tool.isNull(session.getAttribute("sms_count"))||Tool.getDomain().startsWith("192.168")||Tool.getDomain().startsWith("loc"))session.setAttribute("sms_count",4);
        return "/html5/super_login.html";
    }

    @RequestMapping("super_ssoLogin")
    @ResponseBody
    ResultMsg super_ssoLogin(String phone,String code,String name,String pid,HttpSession session){
        if(Tool.isNull(phone))return ResultMsg.fail("请输入手机号",null,null);
        if(Tool.isNull(code))return ResultMsg.fail("请输入验证码",null,null);
        String cache_code = CacheKit.get("CONSTANT", phone);
        if(Tool.isNull(cache_code))return ResultMsg.fail("请先获取验证码",null,null);
        if(!cache_code.equals(code))return ResultMsg.fail("验证码错误",null,null);
        CacheKit.remove("CONSTANT",phone);

        SuperSso superSso=superSsoService.selectOne(new EntityWrapper<SuperSso>().eq("phone",phone));
        if(superSso==null){
            superSso=new SuperSso();
            superSso.setCreateTime(new DateTime());
            superSso.setName(name);
            superSso.setCheck("0");
            superSso.setBalance(0.0);
            superSso.setPhone(phone);
            superSso.setPid(pid);
            superSsoService.insert(superSso);
        }
        if("1".equals(superSso.getCheck())){
//            CacheKit.put("CONSTANT",String.valueOf(superSso.getId()),superSso);
            session.setAttribute(String.valueOf(superSso.getId()),superSso);
        }
        return ResultMsg.success(null,null,superSso);
    }

    @RequestMapping("super_ssoLogin_again")
    @ResponseBody
    ResultMsg super_ssoLogin_again(String phone,HttpSession session){
        if(Tool.isNull(phone))return ResultMsg.fail(null,null,null);
        SuperSso superSso=superSsoService.selectOne(new EntityWrapper<SuperSso>().eq("phone",phone));
        SuperSso session_superSso=superSso!=null?(SuperSso)session.getAttribute(superSso.getId().toString()):null;
        if(session_superSso!=null)return ResultMsg.success(null,null,session_superSso.getId());
        else return ResultMsg.fail(null,null,null);
    }

    @RequestMapping("invite.html")
    String invite(String id,Model model,HttpSession session, HttpServletRequest request,String test){
//        SuperSso superSso=CacheKit.get("CONSTANT",id);
//        SuperSso superSso=Tool.isNull(test)?(SuperSso)session.getAttribute(id):superSsoService.selectById(id);
//        if(!Tool.isNull(test))session.setAttribute(id,superSso);
        SuperSso superSso=(SuperSso)session.getAttribute(id);
        if(superSso==null)return "redirect:/super_ssoApi/super_login";
        model.addAttribute("id",id);
        model.addAllAttributes(dao.selectBySQL("select IFNULL((select count(1)*168 from super_sso_vip where super_sso_id="+id+"),0)amount," +
                "IFNULL((select balance from t_super_sso where id="+id+" limit 1),0)balance," +
                "IFNULL((select count(1) from t_invited where super_sso_id="+id+"),0)invited_num," +
                "IFNULL((select count(1) from super_sso_vip where super_sso_id="+id+"),0)vip_num").get(0));
//        if ("test".equals(test)) {
//            Tool.putWX_config(model, settingService);
//        }
//        else{
            Setting setting=settingService.selectById(1);
            Map<String,Object> wx_config=Tool.getHaveSignatureMap(setting.getWechatTicket(),("http://"+((HttpServletRequest)Tool.getRequest_Response_Session()[0]).getServerName()+((HttpServletRequest)Tool.getRequest_Response_Session()[0]).getRequestURI()+Tool.getUrlRequestParame().get("parameString").toString()));
            wx_config.put("appid",setting.getWechatAppId());
            //签名必须是当前页面， 分享的链接 可以是统一域名下其他的
            wx_config.put("url",("http://"+(request.getServerName()+"/ssoApi/login?super_uid="+id)));
            model.addAttribute("wx_config",wx_config);
            System.err.println("wx_config:********************************************************************************");
            System.err.println(wx_config);
//        }
        return "/html5/invite.html";
    }

    @RequestMapping("invitedByPage")
    @ResponseBody
    public ResultMsg invitedByPage(String super_sso_id,Integer pageNo,Integer pageSize,Integer tabIndex,HttpSession session){
        if(Tool.isNull(pageNo)||Tool.isNull(pageSize)||Tool.isNull(super_sso_id)||Tool.isNull(tabIndex))return ResultMsg.fail("缺少参数",null,null);
//        SuperSso superSso=CacheKit.get("CONSTANT",super_sso_id);
        SuperSso superSso=(SuperSso)session.getAttribute(super_sso_id);
        if(superSso==null)return ResultMsg.fail("请重新登录",null,null);
        ModelMap result=new ModelMap();
        if(pageNo==1)result.addAllAttributes(dao.selectBySQL("select IFNULL((select count(1)*168 from super_sso_vip where super_sso_id="+super_sso_id+"),0)amount," +
                "IFNULL((select balance from t_super_sso where id="+super_sso_id+" limit 1),0)balance," +
                "IFNULL((select count(1) from t_invited where super_sso_id="+super_sso_id+"),0)invited_num," +
                "IFNULL((select count(1) from super_sso_vip where super_sso_id="+super_sso_id+"),0)vip_num").get(0));
        List<Map<String,Object>>list=tabIndex==0?
                dao.selectBySQL("select sso.avatar,sso.phone,sso.nick_name,DATE_FORMAT(sso.create_time,'%Y-%m-%d')`time` from t_invited invited LEFT JOIN t_sso sso ON sso.sso_id=invited.be_sso_id where invited.super_sso_id='"+super_sso_id+"' order by sso.create_time desc limit "+((pageNo-1)*pageSize)+","+pageSize):
                dao.selectBySQL("select sso.avatar,sso.phone,sso.nick_name,DATE_FORMAT(sso_vip.create_time,'%Y-%m-%d')`time` from super_sso_vip sso_vip LEFT JOIN t_sso sso ON sso.sso_id=sso_vip.sso_id where sso_vip.super_sso_id="+super_sso_id+" order by sso_vip.create_time desc limit "+((pageNo-1)*pageSize)+","+pageSize);
        result.addAttribute("data",Tool.listIsNull(list)?new ArrayList<>():list);
        return ResultMsg.success(null,null,result);
    }

    @RequestMapping("my_shouyi.html")
    String my_shouyi(String id, Model model, HttpSession session){
//        SuperSso superSso=CacheKit.get("CONSTANT",id);
        SuperSso superSso=(SuperSso)session.getAttribute(id);
        if(superSso==null)return "redirect:/super_ssoApi/super_login";
        model.addAttribute("id",id);
        model.addAllAttributes(dao.selectBySQL("select IFNULL(balance,0)balance from t_super_sso where id="+id+" limit 1").get(0));
        Tool.putWX_config(model, settingService);
        return "/html5/my_shouyi.html";
    }

    @RequestMapping("withdraw")
    @ResponseBody
    ResultMsg withdraw(String id,HttpSession session){
        if(Tool.isNull(id))return ResultMsg.fail("缺少参数",null,null);
//        SuperSso superSso=CacheKit.get("CONSTANT",id);
        SuperSso superSso=(SuperSso)session.getAttribute(id);
        if(superSso==null)return ResultMsg.fail("请重新登录",null,null);
        SuperSso database_superSso=superSsoService.selectById(id);
        if(Tool.isNull(database_superSso.getBalance())||database_superSso.getBalance()<=0)return ResultMsg.fail("没有可用余额提现",null,null);
        //加入提现申请账目,等待下账--李俊
        UseableBalance useableBalance=new UseableBalance();
        useableBalance.setSuperSsoId(id);
        useableBalance.setPhone(database_superSso.getPhone());
        useableBalance.setUseableBalance(database_superSso.getBalance());
        useableBalance.setState("0");
        useableBalance.setCreateTime(new DateTime());
        useableBalanceService.insert(useableBalance);
        //金额清零
        dao.updateBySQL("UPDATE t_super_sso SET balance=0.0 where id =" + superSso.getId());
        //发短信
        try {
            Setting setting = settingService.selectById(1);
            String responseStr = new DuanXin_LiJun( setting.getYpAppkey(), "3104484").sendAllSms(setting.getTel(),(database_superSso.getBalance()+",订单号:"+useableBalance.getId()),Tool.isNull(database_superSso.getName())?"(合伙人)":(database_superSso.getName()+"(合伙人)"),database_superSso.getPhone());
            System.out.println(responseStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultMsg.success("申请成功",null,null);
    }
}
