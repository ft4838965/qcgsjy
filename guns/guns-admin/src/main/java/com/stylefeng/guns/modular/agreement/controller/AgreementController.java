package com.stylefeng.guns.modular.agreement.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.agreement.service.IAgreementService;
import com.stylefeng.guns.modular.system.model.Agreement;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.util.FSS;
import com.stylefeng.guns.util.SettingConfiguration;
import com.stylefeng.guns.util.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 各种《xxx协议》控制器
 *
 * @author fengshuonan
 * @Date 2018-11-13 14:06:33
 */
@Controller
@RequestMapping("/agreement")
public class AgreementController extends BaseController {

    private String PREFIX = "/agreement/agreement/";

    @Autowired
    private IAgreementService agreementService;

    @Autowired
    public SettingConfiguration settingConfiguration;

    /**
     * 跳转到各种《xxx协议》首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "agreement.html";
    }

    /**
     * 跳转到添加各种《xxx协议》
     */
    @RequestMapping("/agreement_add")
    public String agreementAdd() {
        return PREFIX + "agreement_add.html";
    }

    /**
     * 跳转到修改各种《xxx协议》
     */
    @RequestMapping("/agreement_update/{agreementId}")
    public String agreementUpdate(@PathVariable Integer agreementId, Model model) {
        Agreement agreement = agreementService.selectById(agreementId);
        model.addAttribute("item",agreement);
        LogObjectHolder.me().set(agreement);
        return PREFIX + "agreement_edit.html";
    }

    /**
     * 获取各种《xxx协议》列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<Agreement> page = new PageFactory<Agreement>().defaultPage();
        page=!Tool.isNull(condition)?agreementService.selectPage(page,new EntityWrapper<Agreement>().eq("type",condition)):agreementService.selectPage(page);
        for (Agreement agreement : page.getRecords()) {
            switch (agreement.getType()){
                case "0":agreement.setType("《用户协议》");
                    break;
                case "1":agreement.setType("《隐私政策》");
                    break;
                case "2":agreement.setType("《提现规则》");
                    break;
                case "3":agreement.setType("《邀请好友协议》");
                    break;
                case "4":agreement.setType("《奖励规则》");
                    break;
                case "5":agreement.setType("《排行榜规则》");
                    break;
                default:agreement.setType("<span style='color:red;'>(未知类型协议)</span>");
                    break;
            }
        }
        Map<String,Object> result=new HashMap<>();
        result.put("rows",page.getRecords());
        result.put("total",String.valueOf(page.getTotal()));
        return result;
    }

    /**
     * 新增各种《xxx协议》
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Agreement agreement) {
        if(agreementService.selectCount(new EntityWrapper<Agreement>().eq("type",agreement.getType()))>0)return new ErrorTip(500,"您选择的协议类型已经存在");
        //agreement.setContent(agreement.getContent().replaceAll(" ",""));
        agreementService.insert(agreement);
        return SUCCESS_TIP;
    }

    /**
     * 删除各种《xxx协议》
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer agreementId) {
        Agreement agreement=agreementService.selectById(agreementId);
        Setting setting=settingConfiguration.getSetting();
        if(agreement!=null){
            if(!Tool.isNull(agreement.getContent())){
                Pattern pa = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher m = pa.matcher(agreement.getContent());
                while(m.find()){
                    Tool.deleteAliVideoImgUrl(m.group(1),setting.getAliOssAccessId(),setting.getAliOssAccessKey());
                }
            }

        }
        agreementService.deleteById(agreementId);
        return SUCCESS_TIP;
    }

    /**
     * 修改各种《xxx协议》
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Agreement agreement) {
        int typeCount=agreementService.selectCount(new EntityWrapper<Agreement>().eq("type",agreement.getType()).ne("id",agreement.getId()));
        if(typeCount>0)return new ErrorTip(500,"您选择的协议类型已经存在");
        //agreement.setContent(agreement.getContent().replaceAll(" ",""));
        agreementService.updateById(agreement);
        return SUCCESS_TIP;
    }

    /**
     * 各种《xxx协议》详情
     */
    @RequestMapping(value = "/detail/{agreementId}")
    @ResponseBody
    public Object detail(@PathVariable("agreementId") Integer agreementId) {
        return agreementService.selectById(agreementId);
    }
}
