package com.stylefeng.guns.modular.useable_balance.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.util.ResultMsg;
import com.stylefeng.guns.util.Tool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.UseableBalance;
import com.stylefeng.guns.modular.useable_balance.service.IUseableBalanceService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户申请提现控制器
 *
 * @author fengshuonan
 * @Date 2019-08-22 11:03:14
 */
@Controller
@RequestMapping("/useableBalance")
public class UseableBalanceController extends BaseController {

    private String PREFIX = "/useable_balance/useableBalance/";

    @Autowired
    private IUseableBalanceService useableBalanceService;

    /**
     * 跳转到用户申请提现首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "useableBalance.html";
    }

    /**
     * 跳转到添加用户申请提现
     */
    @RequestMapping("/useableBalance_add")
    public String useableBalanceAdd() {
        return PREFIX + "useableBalance_add.html";
    }

    /**
     * 跳转到修改用户申请提现
     */
    @RequestMapping("/useableBalance_update/{useableBalanceId}")
    public String useableBalanceUpdate(@PathVariable Integer useableBalanceId, Model model) {
        UseableBalance useableBalance = useableBalanceService.selectById(useableBalanceId);
        model.addAttribute("item",useableBalance);
        LogObjectHolder.me().set(useableBalance);
        return PREFIX + "useableBalance_edit.html";
    }
@Autowired
private Dao dao;
    /**
     * 获取用户申请提现列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition,String state,String offset,String limit) {
        List<String> whereSQL=new ArrayList<>();
        if(!Tool.isNull(condition))whereSQL.add("(sso.nick_name LIKE '%"+condition+"%' OR sso.phone LIKE '%"+condition+"%' OR super_sso.`name` LIKE '%"+condition+"%' OR super_sso.phone LIKE '%"+condition+"%')");
        if(!Tool.isNull(state))whereSQL.add("useable_balance.state = '"+state+"'");
        List<Map<String,Object>>useableBalanceByLimit=dao.selectBySQL("SELECT useable_balance.id," +
                "IF(sso.id IS NOT NULL,concat_ws(' | ',sso.nick_name,useable_balance.phone,'(用户)'),if(super_sso.id IS NOT NULL,concat_ws(' | ',super_sso.`name`,useable_balance.phone,'(合伙人)'),concat_ws(' | ','<span style=\"color:red;\">该用户已被删除</span>',useable_balance.phone))) AS ssoId\n" +
                "\t, IF(useable_balance.state = '1', '已支付', IF(useable_balance.state = '0', '待支付', '未知')) AS state\n" +
                "\t, concat(useable_balance.useable_balance, '元') AS useableBalance\n" +
                "\t, DATE_FORMAT(useable_balance.pay_time, '%Y-%m-%d %H:%i:%s') AS payTime\n" +
                "\t, DATE_FORMAT(useable_balance.create_time, '%Y-%m-%d %H:%i:%s') AS createTime\n" +
                "FROM lijun_useable_balance useable_balance\n" +
                "\tLEFT JOIN t_sso sso ON sso.sso_id = useable_balance.sso_id\n" +
                "\tLEFT JOIN t_super_sso super_sso ON super_sso.id = useable_balance.super_sso_id\n" +
                (!Tool.listIsNull(whereSQL)?("WHERE "+ StringUtils.join(whereSQL," AND ")+" \n"):"") +
                "LIMIT "+offset+","+limit);
        List<Map<String,Object>>useable_balancesCount=dao.selectBySQL("SELECT count(1) count\n" +
                "FROM lijun_useable_balance useable_balance\n" +
                "\tLEFT JOIN t_sso sso ON sso.sso_id = useable_balance.sso_id\n" +
                "\tLEFT JOIN t_super_sso super_sso ON super_sso.id = useable_balance.super_sso_id\n" +
                (!Tool.listIsNull(whereSQL)?("WHERE "+ StringUtils.join(whereSQL," AND ")+" \n"):""));
        return new ModelMap("rows",useableBalanceByLimit).addAttribute("total",!Tool.listIsNull(useable_balancesCount)?useable_balancesCount.get(0).get("count"):0);
    }

    /**
     * 新增用户申请提现
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(UseableBalance useableBalance) {
        useableBalanceService.insert(useableBalance);
        return SUCCESS_TIP;
    }

    /**
     * 删除用户申请提现
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer useableBalanceId) {
        useableBalanceService.deleteById(useableBalanceId);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户申请提现
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(UseableBalance useableBalance) {
        useableBalanceService.updateById(useableBalance);
        return SUCCESS_TIP;
    }

    /**
     * 用户申请提现详情
     */
    @RequestMapping(value = "/detail/{useableBalanceId}")
    @ResponseBody
    public Object detail(@PathVariable("useableBalanceId") Integer useableBalanceId) {
        return useableBalanceService.selectById(useableBalanceId);
    }


    @RequestMapping("pay")
    @ResponseBody
    public Object pay(String id){
        try {
            if (Tool.isNull(id))return ResultMsg.fail("缺少参数id", null, null);
            dao.updateBySQL("update lijun_useable_balance set state='1',pay_time='"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new DateTime()) +"' where id="+id);
            return ResultMsg.success("下账成功!",null,null);
        }catch (Exception e){
            e.printStackTrace();
            return ResultMsg.fail("系统错误",null,null);
        }
    }
}
