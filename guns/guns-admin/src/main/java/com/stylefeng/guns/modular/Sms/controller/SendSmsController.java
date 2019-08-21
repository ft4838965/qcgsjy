package com.stylefeng.guns.modular.Sms.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.util.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.SendSms;
import com.stylefeng.guns.modular.Sms.service.ISendSmsService;

import java.util.List;

/**
 * 短信记录控制器
 *
 * @author fengshuonan
 * @Date 2019-03-05 16:24:37
 */
@Controller
@RequestMapping("/sendSms")
public class SendSmsController extends BaseController {

    private String PREFIX = "/Sms/sendSms/";

    @Autowired
    private ISendSmsService sendSmsService;

    /**
     * 跳转到短信记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "sendSms.html";
    }

    /**
     * 跳转到添加短信记录
     */
    @RequestMapping("/sendSms_add")
    public String sendSmsAdd() {
        return PREFIX + "sendSms_add.html";
    }

    /**
     * 跳转到修改短信记录
     */
    @RequestMapping("/sendSms_update/{sendSmsId}")
    public String sendSmsUpdate(@PathVariable Integer sendSmsId, Model model) {
        SendSms sendSms = sendSmsService.selectById(sendSmsId);
        model.addAttribute("item",sendSms);
        LogObjectHolder.me().set(sendSms);
        return PREFIX + "sendSms_edit.html";
    }

    /**
     * 获取短信记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<SendSms> wrapper = new EntityWrapper<>();
        if (!Tool.isNull(condition)){
            wrapper.eq("phone",condition);
        }
        List<SendSms> smsList = sendSmsService.selectList(wrapper);
        for (SendSms s :smsList){
            if ("0".equals(s.getType())){
                s.setType("验证码");
            }else s.setType("消息提醒");
        }
        return smsList;
    }

    /**
     * 新增短信记录
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(SendSms sendSms) {
        sendSmsService.insert(sendSms);
        return SUCCESS_TIP;
    }

    /**
     * 删除短信记录
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer sendSmsId) {
        sendSmsService.deleteById(sendSmsId);
        return SUCCESS_TIP;
    }

    /**
     * 修改短信记录
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(SendSms sendSms) {
        sendSmsService.updateById(sendSms);
        return SUCCESS_TIP;
    }

    /**
     * 短信记录详情
     */
    @RequestMapping(value = "/detail/{sendSmsId}")
    @ResponseBody
    public Object detail(@PathVariable("sendSmsId") Integer sendSmsId) {
        return sendSmsService.selectById(sendSmsId);
    }
}
