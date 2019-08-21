package com.stylefeng.guns.modular.startflow.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.util.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.StartRecord;
import com.stylefeng.guns.modular.startflow.service.IStartRecordService;

import java.util.List;

/**
 * 砖石流水控制器
 *
 * @author fengshuonan
 * @Date 2019-03-06 13:22:58
 */
@Controller
@RequestMapping("/startRecord")
public class StartRecordController extends BaseController {

    private String PREFIX = "/startflow/startRecord/";

    @Autowired
    private IStartRecordService startRecordService;

    /**
     * 跳转到砖石流水首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "startRecord.html";
    }

    /**
     * 跳转到添加砖石流水
     */
    @RequestMapping("/startRecord_add")
    public String startRecordAdd() {
        return PREFIX + "startRecord_add.html";
    }

    /**
     * 跳转到修改砖石流水
     */
    @RequestMapping("/startRecord_update/{startRecordId}")
    public String startRecordUpdate(@PathVariable Integer startRecordId, Model model) {
        StartRecord startRecord = startRecordService.selectById(startRecordId);
        model.addAttribute("item",startRecord);
        LogObjectHolder.me().set(startRecord);
        return PREFIX + "startRecord_edit.html";
    }

    /**
     * 获取砖石流水列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<StartRecord> wrapper = new EntityWrapper<>();
        if (!Tool.isNull(condition)){
            wrapper.eq("sso_id",condition);
            wrapper.or();
            wrapper.eq("girl_id",condition);
        }
        List<StartRecord> startRecords = startRecordService.selectList(wrapper);
        return startRecords;
    }

    /**
     * 新增砖石流水
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(StartRecord startRecord) {
        startRecord.setCreateTime(new DateTime());
        startRecordService.insert(startRecord);
        return SUCCESS_TIP;
    }

    /**
     * 删除砖石流水
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer startRecordId) {
        startRecordService.deleteById(startRecordId);
        return SUCCESS_TIP;
    }

    /**
     * 修改砖石流水
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(StartRecord startRecord) {
        startRecordService.updateById(startRecord);
        return SUCCESS_TIP;
    }

    /**
     * 砖石流水详情
     */
    @RequestMapping(value = "/detail/{startRecordId}")
    @ResponseBody
    public Object detail(@PathVariable("startRecordId") Integer startRecordId) {
        return startRecordService.selectById(startRecordId);
    }
}
