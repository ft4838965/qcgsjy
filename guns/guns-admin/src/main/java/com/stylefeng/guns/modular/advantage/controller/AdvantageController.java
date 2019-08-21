package com.stylefeng.guns.modular.advantage.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Advantage;
import com.stylefeng.guns.modular.advantage.service.IAdvantageService;

/**
 * 个人优势控制器
 *
 * @author fengshuonan
 * @Date 2019-03-12 11:06:22
 */
@Controller
@RequestMapping("/advantage")
public class AdvantageController extends BaseController {

    private String PREFIX = "/advantage/advantage/";

    @Autowired
    private IAdvantageService advantageService;

    /**
     * 跳转到个人优势首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "advantage.html";
    }

    /**
     * 跳转到添加个人优势
     */
    @RequestMapping("/advantage_add")
    public String advantageAdd() {
        return PREFIX + "advantage_add.html";
    }

    /**
     * 跳转到修改个人优势
     */
    @RequestMapping("/advantage_update/{advantageId}")
    public String advantageUpdate(@PathVariable Integer advantageId, Model model) {
        Advantage advantage = advantageService.selectById(advantageId);
        model.addAttribute("item",advantage);
        LogObjectHolder.me().set(advantage);
        return PREFIX + "advantage_edit.html";
    }

    /**
     * 获取个人优势列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return advantageService.selectList(null);
    }

    /**
     * 新增个人优势
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Advantage advantage) {
        advantageService.insert(advantage);
        return SUCCESS_TIP;
    }

    /**
     * 删除个人优势
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer advantageId) {
        advantageService.deleteById(advantageId);
        return SUCCESS_TIP;
    }

    /**
     * 修改个人优势
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Advantage advantage) {
        advantageService.updateById(advantage);
        return SUCCESS_TIP;
    }

    /**
     * 个人优势详情
     */
    @RequestMapping(value = "/detail/{advantageId}")
    @ResponseBody
    public Object detail(@PathVariable("advantageId") Integer advantageId) {
        return advantageService.selectById(advantageId);
    }
}
