package com.stylefeng.guns.modular.paybill.controller;

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
import com.stylefeng.guns.modular.system.model.PayBill;
import com.stylefeng.guns.modular.paybill.service.IPayBillService;

import java.util.List;

/**
 * 流水管理控制器
 *
 * @author fengshuonan
 * @Date 2019-03-05 17:17:11
 */
@Controller
@RequestMapping("/payBill")
public class PayBillController extends BaseController {

    private String PREFIX = "/paybill/payBill/";

    @Autowired
    private IPayBillService payBillService;

    /**
     * 跳转到流水管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "payBill.html";
    }

    /**
     * 跳转到添加流水管理
     */
    @RequestMapping("/payBill_add")
    public String payBillAdd() {
        return PREFIX + "payBill_add.html";
    }

    /**
     * 跳转到修改流水管理
     */
    @RequestMapping("/payBill_update/{payBillId}")
    public String payBillUpdate(@PathVariable Integer payBillId, Model model) {
        PayBill o = payBillService.selectById(payBillId);
        if ("1".equals(o.getPayState())) {
            o.setPayState("已支付");
        }else o.setPayState("未支付");

        if ("1".equals(o.getPayType())) {
            o.setPayType("支付宝");
        }else o.setPayType("微信支付");
        model.addAttribute("item",o);
        LogObjectHolder.me().set(o);
        return PREFIX + "payBill_edit.html";
    }

    /**
     * 获取流水管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        EntityWrapper<PayBill> wrapper = new EntityWrapper<>();
        if (!Tool.isNull(condition)){
            wrapper.eq("sso_id",condition);
        }
        List<PayBill> payBills = payBillService.selectList(wrapper);
        for (PayBill o : payBills){
            if ("1".equals(o.getPayState())) {
                o.setPayState("已支付");
            }else o.setPayState("未支付");

            if ("1".equals(o.getPayType())) {
                o.setPayType("支付宝");
            }else o.setPayType("微信支付");
        }
        return payBills;
    }

    /**
     * 新增流水管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(PayBill payBill) {
        payBill.setCreateTime(new DateTime());
        payBillService.insert(payBill);
        return SUCCESS_TIP;
    }

    /**
     * 删除流水管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer payBillId) {
        payBillService.deleteById(payBillId);
        return SUCCESS_TIP;
    }

    /**
     * 修改流水管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(PayBill payBill) {
        payBillService.updateById(payBill);
        return SUCCESS_TIP;
    }

    /**
     * 流水管理详情
     */
    @RequestMapping(value = "/detail/{payBillId}")
    @ResponseBody
    public Object detail(@PathVariable("payBillId") Integer payBillId) {
        return payBillService.selectById(payBillId);
    }
}
