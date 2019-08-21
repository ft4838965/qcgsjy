package com.stylefeng.guns.modular.ssoaccoun.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.system.model.Order;
import com.stylefeng.guns.util.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.SsoAccountFlow;
import com.stylefeng.guns.modular.ssoaccoun.service.ISsoAccountFlowService;

import java.util.List;

/**
 * 打赏提现控制器
 *
 * @author fengshuonan
 * @Date 2019-03-05 18:21:11
 */
@Controller
@RequestMapping("/ssoAccountFlow")
public class SsoAccountFlowController extends BaseController {

    private String PREFIX = "/ssoaccoun/ssoAccountFlow/";

    @Autowired
    private ISsoAccountFlowService ssoAccountFlowService;

    /**
     * 跳转到打赏提现首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "ssoAccountFlow.html";
    }

    /**
     * 跳转到添加打赏提现
     */
    @RequestMapping("/ssoAccountFlow_add")
    public String ssoAccountFlowAdd() {
        return PREFIX + "ssoAccountFlow_add.html";
    }

    /**
     * 跳转到修改打赏提现
     */
    @RequestMapping("/ssoAccountFlow_update/{ssoAccountFlowId}")
    public String ssoAccountFlowUpdate(@PathVariable Integer ssoAccountFlowId, Model model) {
        SsoAccountFlow ssoAccountFlow = ssoAccountFlowService.selectById(ssoAccountFlowId);
        if ("0".equals(ssoAccountFlow.getComeFrom())) {
            ssoAccountFlow.setComeFrom("官方");
        }else ssoAccountFlow.setComeFrom("用户："+ssoAccountFlow.getComeFrom());
        model.addAttribute("item",ssoAccountFlow);
        LogObjectHolder.me().set(ssoAccountFlow);
        return PREFIX + "ssoAccountFlow_edit.html";
    }

    /**
     * 获取打赏提现列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<SsoAccountFlow> wrapper = new EntityWrapper<>();
        if (!Tool.isNull(condition)){
            wrapper.eq("sso_id",condition);
        }
        List<SsoAccountFlow> ssoAccountFlows = ssoAccountFlowService.selectList(wrapper);
        for (SsoAccountFlow saf : ssoAccountFlows){
            if ("0".equals(saf.getComeFrom())) {
                saf.setComeFrom("官方");
            }else saf.setComeFrom("用户："+saf.getComeFrom());
        }
        return ssoAccountFlows;
    }

    /**
     * 新增打赏提现
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(SsoAccountFlow ssoAccountFlow) {
        ssoAccountFlow.setCreateTime(new DateTime());
        ssoAccountFlowService.insert(ssoAccountFlow);
        return SUCCESS_TIP;
    }

    /**
     * 删除打赏提现
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer ssoAccountFlowId) {
        ssoAccountFlowService.deleteById(ssoAccountFlowId);
        return SUCCESS_TIP;
    }

    /**
     * 修改打赏提现
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(SsoAccountFlow ssoAccountFlow) {
        ssoAccountFlowService.updateById(ssoAccountFlow);
        return SUCCESS_TIP;
    }

    /**
     * 打赏提现详情
     */
    @RequestMapping(value = "/detail/{ssoAccountFlowId}")
    @ResponseBody
    public Object detail(@PathVariable("ssoAccountFlowId") Integer ssoAccountFlowId) {
        return ssoAccountFlowService.selectById(ssoAccountFlowId);
    }
}
