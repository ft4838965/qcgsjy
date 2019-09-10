package com.stylefeng.guns.modular.order.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.util.Tool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.Order;
import com.stylefeng.guns.modular.order.service.IOrderService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 订单管理控制器
 *
 * @author fengshuonan
 * @Date 2019-03-26 09:54:52
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    private String PREFIX = "/order/order/";

    @Autowired
    private IOrderService orderService;

    /**
     * 跳转到订单管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "order.html";
    }

    /**
     * 跳转到添加订单管理
     */
    @RequestMapping("/order_add")
    public String orderAdd() {
        return PREFIX + "order_add.html";
    }

    /**
     * 跳转到修改订单管理
     */
    @RequestMapping("/order_update/{orderId}")
    public String orderUpdate(@PathVariable Integer orderId, Model model) {
        Order order = orderService.selectById(orderId);
        model.addAttribute("item",order);
        LogObjectHolder.me().set(order);
        return PREFIX + "order_edit.html";
    }
    @Autowired
    private Dao dao;
    /**
     * 获取订单管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition,String state,String offset,String limit,String start_time,String end_time) {
        List<String>whereSQL=new ArrayList<>();
        if(!Tool.isNull(condition))whereSQL.add("(sso.nick_name LIKE '%"+condition+"%' OR sso.phone LIKE '%"+condition+"%')");
        if(!Tool.isNull(state))whereSQL.add("`order`.state = '"+state+"'");
        if(!Tool.isNull(start_time))whereSQL.add("`order`.create_time >= '"+start_time+"'");
        if(!Tool.isNull(end_time))whereSQL.add("`order`.finished_time <= '"+end_time+" 23:59:59'");
        List<Map<String,Object>>ordersByLimit=dao.selectBySQL("SELECT `order`.id,if(sso.id is null,'<span style=\"color:red;\">该用户已被删除</span>',concat_ws(' | ', sso.nick_name, sso.phone)) AS ssoId\n" +
                "\t, IF(`order`.state = '1', '已支付', IF(`order`.state = '0', '待支付', '未知')) AS state\n" +
                "\t, concat(`order`.pay_money, '元') AS payMoney\n" +
                "\t, IF(`order`.pay_channel = 'wechat', '微信', IF(`order`.pay_channel = 'ali', '支付宝', '其他')) AS payChannel\n" +
                "\t, `order`.order_from orderFrom\n"+
                "\t, DATE_FORMAT(`order`.pay_time, '%Y-%m-%d %H:%i:%s') AS payTime\n" +
                "\t, DATE_FORMAT(`order`.finished_time, '%Y-%m-%d %H:%i:%s') AS finishedTime\n" +
                "FROM t_order `order`\n" +
                "\tLEFT JOIN t_sso sso ON sso.sso_id = `order`.sso_id\n" +
                (!Tool.listIsNull(whereSQL)?("WHERE "+ StringUtils.join(whereSQL," AND ")+" \n"):"") +
                "  order by `order`.create_time ASC "+
                "LIMIT "+offset+","+limit);
        ordersByLimit.parallelStream().forEach(order->{
            if(!Tool.isNull(order.get("orderFrom"))){
                List<String>orderFroms=new ArrayList<>(Arrays.asList(order.get("orderFrom").toString().split(":")));
                order.put("orderFrom",((orderFroms.contains("buyVip")?"购买会员":"")+(orderFroms.size()>3?orderFroms.get(3):"")));
            }
        });
        List<Map<String,Object>>ordersCount=dao.selectBySQL("SELECT count(1) count\n" +
                "FROM t_order `order`\n" +
                "\tLEFT JOIN t_sso sso ON sso.sso_id = `order`.sso_id\n" +
                (!Tool.listIsNull(whereSQL)?("WHERE "+ StringUtils.join(whereSQL," AND ")+" \n"):""));
        return new ModelMap("rows",ordersByLimit).addAttribute("total",!Tool.listIsNull(ordersCount)?ordersCount.get(0).get("count"):0);
    }

    /**
     * 新增订单管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Order order) {
        orderService.insert(order);
        return SUCCESS_TIP;
    }

    /**
     * 删除订单管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer orderId) {
        orderService.deleteById(orderId);
        return SUCCESS_TIP;
    }

    /**
     * 修改订单管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Order order) {
        orderService.updateById(order);
        return SUCCESS_TIP;
    }

    /**
     * 订单管理详情
     */
    @RequestMapping(value = "/detail/{orderId}")
    @ResponseBody
    public Object detail(@PathVariable("orderId") Integer orderId) {
        return orderService.selectById(orderId);
    }
}
