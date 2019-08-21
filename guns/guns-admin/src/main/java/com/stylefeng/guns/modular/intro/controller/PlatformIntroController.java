package com.stylefeng.guns.modular.intro.controller;

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
import com.stylefeng.guns.modular.system.model.PlatformIntro;
import com.stylefeng.guns.modular.intro.service.IPlatformIntroService;

import java.util.List;

/**
 * 平台介绍控制器
 *
 * @author fengshuonan
 * @Date 2019-03-06 13:47:55
 */
@Controller
@RequestMapping("/platformIntro")
public class PlatformIntroController extends BaseController {

    private String PREFIX = "/intro/platformIntro/";

    @Autowired
    private IPlatformIntroService platformIntroService;

    /**
     * 跳转到平台介绍首页
     */
    @RequestMapping("")
    public String index(Model model)
    {
        List<PlatformIntro> platformIntros = platformIntroService.selectList(null);
        PlatformIntro platformIntro = new PlatformIntro();
        if (!Tool.listIsNull(platformIntros)){
            platformIntro = platformIntros.get(0);
        }
        model.addAttribute("item",platformIntro);
        return PREFIX + "platformIntro_edit.html";
    }

    /**
     * 跳转到添加平台介绍
     */
    @RequestMapping("/platformIntro_add")
    public String platformIntroAdd() {
        return PREFIX + "platformIntro_add.html";
    }

    /**
     * 跳转到修改平台介绍
     */
    @RequestMapping("/platformIntro_update/{platformIntroId}")
    public String platformIntroUpdate(@PathVariable Integer platformIntroId, Model model) {
        PlatformIntro platformIntro = platformIntroService.selectById(platformIntroId);
        model.addAttribute("item",platformIntro);
        LogObjectHolder.me().set(platformIntro);
        return PREFIX + "platformIntro_edit.html";
    }

    /**
     * 获取平台介绍列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return platformIntroService.selectList(null);
    }

    /**
     * 新增平台介绍
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(PlatformIntro platformIntro) {
        platformIntro.setIntro(platformIntro.getIntro().replaceAll(" ",""));
        platformIntroService.insert(platformIntro);
        return SUCCESS_TIP;
    }

    /**
     * 删除平台介绍
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer platformIntroId) {
        platformIntroService.deleteById(platformIntroId);
        return SUCCESS_TIP;
    }

    /**
     * 修改平台介绍
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(PlatformIntro platformIntro) {
        //platformIntro.setIntro(platformIntro.getIntro().replaceAll(" ",""));
        platformIntroService.updateById(platformIntro);
        return SUCCESS_TIP;
    }

    /**
     * 平台介绍详情
     */
    @RequestMapping(value = "/detail/{platformIntroId}")
    @ResponseBody
    public Object detail(@PathVariable("platformIntroId") Integer platformIntroId) {
        return platformIntroService.selectById(platformIntroId);
    }
}
