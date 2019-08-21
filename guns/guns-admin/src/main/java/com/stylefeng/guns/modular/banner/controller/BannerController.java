package com.stylefeng.guns.modular.banner.controller;

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
import com.stylefeng.guns.modular.system.model.Banner;
import com.stylefeng.guns.modular.banner.service.IBannerService;

import java.util.List;

/**
 * UI管理控制器
 *
 * @author fengshuonan
 * @Date 2019-03-12 14:39:10
 */
@Controller
@RequestMapping("/banner")
public class BannerController extends BaseController {

    private String PREFIX = "/banner/banner/";

    @Autowired
    private IBannerService bannerService;

    /**
     * 跳转到UI管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "banner.html";
    }

    /**
     * 跳转到添加UI管理
     */
    @RequestMapping("/banner_add")
    public String bannerAdd() {
        return PREFIX + "banner_add.html";
    }

    /**
     * 跳转到修改UI管理
     */
    @RequestMapping("/banner_update/{bannerId}")
    public String bannerUpdate(@PathVariable Integer bannerId, Model model) {
        Banner banner = bannerService.selectById(bannerId);
        model.addAttribute("item",banner);
        LogObjectHolder.me().set(banner);
        return PREFIX + "banner_edit.html";
    }

    /**
     * 获取UI管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition)
    {
        List<Banner> banners = bannerService.selectList(null);
        for (Banner banner : banners){
            banner.setPath("<img src='"+banner.getPath()+"' style='width:80%;height:200px;'/>");
        }

        return banners;
    }

    /**
     * 新增UI管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Banner banner) {
        banner.setCreateTime(new DateTime());
        bannerService.insert(banner);
        return SUCCESS_TIP;
    }

    /**
     * 删除UI管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bannerId) {
        bannerService.deleteById(bannerId);
        return SUCCESS_TIP;
    }

    /**
     * 修改UI管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Banner banner) {
        bannerService.updateById(banner);
        return SUCCESS_TIP;
    }

    /**
     * UI管理详情
     */
    @RequestMapping(value = "/detail/{bannerId}")
    @ResponseBody
    public Object detail(@PathVariable("bannerId") Integer bannerId) {
        return bannerService.selectById(bannerId);
    }
}
