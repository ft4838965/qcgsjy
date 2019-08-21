package com.stylefeng.guns.modular.invited.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.message.service.IMessageService;
import com.stylefeng.guns.modular.setting.service.ISettingService;
import com.stylefeng.guns.modular.ssoaccoun.service.ISsoAccountFlowService;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.system.model.Message;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.modular.system.model.SsoAccountFlow;
import com.stylefeng.guns.util.SettingConfiguration;
import com.stylefeng.guns.util.Tool;
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
import com.stylefeng.guns.modular.system.model.Invited;
import com.stylefeng.guns.modular.invited.service.IInvitedService;

import java.util.List;
import java.util.Map;

/**
 * 邀请返现控制器
 *
 * @author fengshuonan
 * @Date 2019-03-21 11:28:48
 */
@Controller
@RequestMapping("/invited")
public class InvitedController extends BaseController {

    private String PREFIX = "/invited/invited/";

    @Autowired
    private IInvitedService invitedService;

    @Autowired
    private Dao dao;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private ISsoAccountFlowService accountFlowService;

    @Autowired
    private ISettingService settingService;


    /**
     * 跳转到邀请返现首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "invited.html";
    }

    /**
     * 跳转到添加邀请返现
     */
    @RequestMapping("/invited_add")
    public String invitedAdd() {
        return PREFIX + "invited_add.html";
    }

    /**
     * 跳转到修改邀请返现
     */
    @RequestMapping("/invited_update/{invitedId}")
    public String invitedUpdate(@PathVariable Integer invitedId, Model model) {
        Invited invited = invitedService.selectById(invitedId);
        model.addAttribute("item",invited);
        LogObjectHolder.me().set(invited);
        return PREFIX + "invited_edit.html";
    }

    /**
     * 获取邀请返现列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> maps = dao.selectBySQL("SELECT\n" +
                "\ta.id,\n" +
                "\ta.sso_id AS ssoId,\n" +
                "\ta.be_sso_id AS beSsoId,\n" +
                "\ta.create_time AS createTime,\n" +
                "\tIF(a.status = '0','未结算','已结算') AS status,\n" +
                "\tIF(b.type_id is not null,200,0) AS ssoSpend \n" +
                "FROM\n" +
                "\tt_invited a\n" +
                "\tLEFT JOIN t_vip b ON a.sso_id = b.sso_id\n" +
                "\tLEFT JOIN t_sso c ON a.sso_id = c.sso_id \n" +
                "WHERE\n" +
                "\tc.sex = '1' order by a.create_time desc");
        for (Map<String, Object> m : maps){
            String beSsoId = m.get("beSsoId").toString();
            List<Map<String, Object>> maps1 = dao.selectBySQL("SELECT\n" +
                    "IF\n" +
                    "\t( c.sex = '0', '男', '女' ) AS sex,\n" +
                    "\t(\n" +
                    "CASE\n" +
                    "\tb.type_id \n" +
                    "\tWHEN '1' THEN\n" +
                    "\t200 \n" +
                    "\tWHEN '2' THEN\n" +
                    "\t500 \n" +
                    "\tWHEN '3' THEN\n" +
                    "\t900 ELSE 0 \n" +
                    "END \n" +
                    "\t) AS beSsoSpend \n" +
                    "FROM\n" +
                    "\tt_sso c\n" +
                    "\tLEFT JOIN t_vip b ON b.sso_id = c.sso_id \n" +
                    "WHERE\n" +
                    "\t c.sso_id = " + beSsoId);
            m.put("sex",maps1.get(0).get("sex"));
            m.put("beSsoSpend",maps1.get(0).get("beSsoSpend"));
        }
        return maps;
    }

    /**
     *  返现
     */
    @RequestMapping(value = "/giveMoney")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object cleanMoney(@RequestParam Integer id) {
        Invited invited = invitedService.selectById(id);
        if (invited.getStatus().equals("0")){
            Setting setting = settingService.selectById(1);
            //返现
            dao.updateBySQL("UPDATE t_sso_account a LEFT JOIN t_invited b ON a.sso_id = b.sso_id SET a.useable_balance = a.useable_balance +" + setting.getMoneyGiveWomen()+ " WHERE b.id ="+id);
            //创建账户流水
            SsoAccountFlow ssoAccountFlow = new SsoAccountFlow();
            ssoAccountFlow.setMoney(50.0);
            ssoAccountFlow.setNote("邀请返现");
            ssoAccountFlow.setBusinessType("0");
            ssoAccountFlow.setBusinessName("返现");
            ssoAccountFlow.setComeFrom(invited.getBeSsoId());
            ssoAccountFlow.setSsoId(Integer.parseInt(invited.getSsoId()));
            ssoAccountFlow.setCreateTime(new DateTime());
            accountFlowService.insert(ssoAccountFlow);
            //创建消息
            Message message = new Message();
            message.setContent("您邀请ID为"+invited.getBeSsoId()+"的用户成功！返现50￥，请查收！");
            message.setCreateTime(new DateTime());
            message.setLook("0");
            message.setSsoId(0);
            message.setMessageSsoId(invited.getSsoId());
            message.setType("2");
            message.setOfficialMessageType("4");
            messageService.insert(message);
            invited.setStatus("1");
            invitedService.updateById(invited);
        }
        return SUCCESS_TIP;
    }

    /**
     * 新增邀请返现
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Invited invited) {
        invitedService.insert(invited);
        return SUCCESS_TIP;
    }

    /**
     * 删除邀请返现
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer invitedId) {
        invitedService.deleteById(invitedId);
        return SUCCESS_TIP;
    }

    /**
     * 修改邀请返现
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Invited invited) {
        invitedService.updateById(invited);
        return SUCCESS_TIP;
    }

    /**
     * 邀请返现详情
     */
    @RequestMapping(value = "/detail/{invitedId}")
    @ResponseBody
    public Object detail(@PathVariable("invitedId") Integer invitedId) {
        return invitedService.selectById(invitedId);
    }
}
