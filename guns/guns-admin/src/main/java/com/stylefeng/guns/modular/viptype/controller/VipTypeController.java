package com.stylefeng.guns.modular.viptype.controller;

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
import com.stylefeng.guns.modular.system.model.VipType;
import com.stylefeng.guns.modular.viptype.service.IVipTypeService;

/**
 * 会员类型控制器
 *
 * @author fengshuonan
 * @Date 2019-03-01 17:16:37
 */
@Controller
@RequestMapping("/vipType")
public class VipTypeController extends BaseController {

    private String PREFIX = "/viptype/vipType/";

    @Autowired
    private IVipTypeService vipTypeService;

    /**
     * 跳转到会员类型首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "vipType.html";
    }

    /**
     * 跳转到添加会员类型
     */
    @RequestMapping("/vipType_add")
    public String vipTypeAdd() {
        return PREFIX + "vipType_add.html";
    }

    /**
     * 跳转到修改会员类型
     */
    @RequestMapping("/vipType_update/{vipTypeId}")
    public String vipTypeUpdate(@PathVariable Integer vipTypeId, Model model) {
        VipType vipType = vipTypeService.selectById(vipTypeId);
        model.addAttribute("item",vipType);
        LogObjectHolder.me().set(vipType);
        return PREFIX + "vipType_edit.html";
    }

    /**
     * 获取会员类型列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return vipTypeService.selectList(null);
    }

    /**
     * 新增会员类型
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(VipType vipType) {
        vipType.setCreateTime(new DateTime());
        vipTypeService.insert(vipType);
        return SUCCESS_TIP;
    }

    /**
     * 删除会员类型
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer vipTypeId) {
        vipTypeService.deleteById(vipTypeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改会员类型
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(VipType vipType) {
        vipTypeService.updateById(vipType);
        return SUCCESS_TIP;
    }

    /**
     * 会员类型详情
     */
    @RequestMapping(value = "/detail/{vipTypeId}")
    @ResponseBody
    public Object detail(@PathVariable("vipTypeId") Integer vipTypeId) {
        return vipTypeService.selectById(vipTypeId);
    }
}
