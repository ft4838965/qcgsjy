package com.stylefeng.guns.modular.supersso.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.support.DateTime;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.util.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.SuperSso;
import com.stylefeng.guns.modular.supersso.service.ISuperSsoService;

import java.util.List;

/**
 * 合伙人控制器
 *
 * @author fengshuonan
 * @Date 2019-03-12 11:19:34
 */
@Controller
@RequestMapping("/superSso")
public class SuperSsoController extends BaseController {

    private String PREFIX = "/supersso/superSso/";

    @Autowired
    private ISuperSsoService superSsoService;
    @Autowired
    private Dao dao;
    /**
     * 跳转到合伙人首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "superSso.html";
    }

    /**
     * 跳转到添加合伙人
     */
    @RequestMapping("/superSso_add")
    public String superSsoAdd() {
        return PREFIX + "superSso_add.html";
    }

    /**
     * 跳转到修改合伙人
     */
    @RequestMapping("/superSso_update/{superSsoId}")
    public String superSsoUpdate(@PathVariable Integer superSsoId, Model model) {
        SuperSso superSso = superSsoService.selectById(superSsoId);
        model.addAttribute("item",superSso);
        LogObjectHolder.me().set(superSso);
        return PREFIX + "superSso_edit.html";
    }

    /**
     * 获取合伙人列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
//        List<SuperSso> superSsoList=superSsoService.selectList(Tool.isNull(condition)?null:new EntityWrapper<SuperSso>().eq("name",condition));
//        superSsoList.parallelStream().forEach(superSso->superSso.setCheck("1".equals(superSso.getCheck())?"<span style='color:green'>已审核</span>":"<span style='color:red'>待审核</span>"));
//        return superSsoList;
        return dao.selectBySQL("select a.id,a.`name`,a.phone,if(a.`check`='1','<span style=\"color:green\">已审核</span>','<span style=\"color:red\">待审核</span>')`check`,a.balance,(b.`name`)pid from t_super_sso a left join t_super_sso b ON b.id=a.pid");
    }

    /**
     * 新增合伙人
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(SuperSso superSso) {
        if(Tool.isNull(superSso.getPhone()))return new ErrorTip(400,"请输入手机号");
        SuperSso tempSuperSso=superSsoService.selectOne(new EntityWrapper<SuperSso>().eq("phone",superSso.getPhone()));
        if(tempSuperSso!=null)return new ErrorTip(500,"该手机号用户已存在");
        superSso.setCreateTime(new DateTime());
        superSsoService.insert(superSso);
        return SUCCESS_TIP;
    }

    /**
     * 删除合伙人
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer superSsoId) {
        superSsoService.deleteById(superSsoId);
        return SUCCESS_TIP;
    }

    /**
     * 修改合伙人
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(SuperSso superSso) {
        superSsoService.updateById(superSso);
        return SUCCESS_TIP;
    }

    /**
     * 合伙人详情
     */
    @RequestMapping(value = "/detail/{superSsoId}")
    @ResponseBody
    public Object detail(@PathVariable("superSsoId") Integer superSsoId) {
        return superSsoService.selectById(superSsoId);
    }
}
