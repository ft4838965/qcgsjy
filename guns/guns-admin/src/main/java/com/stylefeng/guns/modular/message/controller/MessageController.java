package com.stylefeng.guns.modular.message.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Message;
import com.stylefeng.guns.modular.message.service.IMessageService;

import java.util.List;

/**
 * 消息管理控制器
 *
 * @author fengshuonan
 * @Date 2019-03-05 11:56:49
 */
@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {

    private String PREFIX = "/message/message/";

    @Autowired
    private IMessageService messageService;

    /**
     * 跳转到消息管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "message.html";
    }

    /**
     * 跳转到添加消息管理
     */
    @RequestMapping("/message_add")
    public String messageAdd() {
        return PREFIX + "message_add.html";
    }

    /**
     * 跳转到修改消息管理
     */
    @RequestMapping("/message_update/{messageId}")
    public String messageUpdate(@PathVariable Integer messageId, Model model) {
        Message message = messageService.selectById(messageId);
        model.addAttribute("item",message);
        LogObjectHolder.me().set(message);
        return PREFIX + "message_edit.html";
    }

    /**
     * 获取消息管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<Message> wrapper = new EntityWrapper<>();
        wrapper.eq("sso_id","0");
        wrapper.eq("message_sso_id","0");
        wrapper.or("message_sso_id=1 or message_sso_id=2");
        List<Message> messageList = messageService.selectList(wrapper);
        for (Message m :messageList){
            if("0".equals(m.getMessageSsoId())){
                m.setMessageSsoId("男性用户");
            }else if("1".equals(m.getMessageSsoId())){
                m.setMessageSsoId("女性用户");
            }else if("2".equals(m.getMessageSsoId())){
                m.setMessageSsoId("所有用户");
            }
        }
        return messageList;
    }

    /**
     * 新增消息管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Message message) {
        message.setCreateTime(new DateTime());
        messageService.insert(message);
        return SUCCESS_TIP;
    }

    /**
     * 删除消息管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer messageId) {
        messageService.deleteById(messageId);
        return SUCCESS_TIP;
    }

    /**
     * 修改消息管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Message message) {
        messageService.updateById(message);
        return SUCCESS_TIP;
    }

    /**
     * 消息管理详情
     */
    @RequestMapping(value = "/detail/{messageId}")
    @ResponseBody
    public Object detail(@PathVariable("messageId") Integer messageId) {
        return messageService.selectById(messageId);
    }
}
