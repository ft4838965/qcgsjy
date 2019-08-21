package com.stylefeng.guns.modular.quartzjob.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.QuartzJob;
import com.stylefeng.guns.modular.quartzjob.service.IQuartzJobService;

/**
 * 定时任务控制器
 *
 * @author fengshuonan
 * @Date 2019-03-26 10:18:16
 */
@Controller
@RequestMapping("/quartzJob")
public class QuartzJobController extends BaseController {

    private String PREFIX = "/quartzjob/quartzJob/";

    @Autowired
    private IQuartzJobService quartzJobService;

    /**
     * 跳转到定时任务首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "quartzJob.html";
    }

    /**
     * 跳转到添加定时任务
     */
    @RequestMapping("/quartzJob_add")
    public String quartzJobAdd() {
        return PREFIX + "quartzJob_add.html";
    }

    /**
     * 跳转到修改定时任务
     */
    @RequestMapping("/quartzJob_update/{quartzJobId}")
    public String quartzJobUpdate(@PathVariable Integer quartzJobId, Model model) {
        QuartzJob quartzJob = quartzJobService.selectById(quartzJobId);
        model.addAttribute("item",quartzJob);
        LogObjectHolder.me().set(quartzJob);
        return PREFIX + "quartzJob_edit.html";
    }

    /**
     * 获取定时任务列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return quartzJobService.selectList(null);
    }

    /**
     * 新增定时任务
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(QuartzJob quartzJob) {
        quartzJobService.insert(quartzJob);
        return SUCCESS_TIP;
    }

    /**
     * 删除定时任务
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer quartzJobId) {
        quartzJobService.deleteById(quartzJobId);
        return SUCCESS_TIP;
    }

    /**
     * 修改定时任务
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(QuartzJob quartzJob) {
        quartzJobService.updateById(quartzJob);
        return SUCCESS_TIP;
    }

    /**
     * 定时任务详情
     */
    @RequestMapping(value = "/detail/{quartzJobId}")
    @ResponseBody
    public Object detail(@PathVariable("quartzJobId") Integer quartzJobId) {
        return quartzJobService.selectById(quartzJobId);
    }
}
