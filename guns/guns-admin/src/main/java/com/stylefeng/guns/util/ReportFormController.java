/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: ReportFormController
 * Author:   Arron-Wu
 * Date:     2019/3/4 0:23
 * Description: 导出Excel表单控制器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.stylefeng.guns.util;

import com.stylefeng.guns.modular.account.service.ISsoAccountService;
import com.stylefeng.guns.modular.man.service.ISsoService;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.modular.women.service.ISsoInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈导出Excel表单控制器〉
 *
 * @author Arron
 * @create 2019/3/4
 * @since 1.0.0
 */
@Controller
@RequestMapping(value = "/export")
public class ReportFormController {

    @Autowired
    private ISsoInfoService ssoInfoService;

    @Autowired
    private ISsoAccountService ssoAccountService;

    @Autowired
    private ISsoService ssoService;

    @Autowired
    private Dao dao;


    /**
     * 导出女性用户报表
     * @return
     */
    @RequestMapping(value = "/girls")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取数据
        //List<PageData> list = reportService.bookList(page);
        List<Map<String,Object>> arron = new ArrayList<>();
        String sql = "SELECT\n" +
                "  b.id,\n" +
                "\ta.sso_id as ssoId,\n" +
                "  a.phone,\n" +
                "  a.nick_name as nickName,\n" +
                "  a.create_time as createTime,\n" +
                "  b.birthday,\n" +
                "  b.age,\n" +
                "  b.advantege,\n" +
                "  b.tall,\n" +
                "  b.weight,\n" +
                "  c.useable_balance as total\n" +
                "FROM\n" +
                "\tt_sso a\n" +
                "LEFT JOIN t_sso_info b ON a.sso_id = b.sso_id\n" +
                "LEFT JOIN t_sso_account c ON b.sso_id = c.sso_id\n" +
                "where a.sex = '1' ";
        arron = dao.selectBySQL(sql);

        //excel标题
        String[] title = {"手机号","昵称","年龄","身高","体重","打赏总额","加入时间"};

         //excel文件名
        String fileName = "女会员表"+System.currentTimeMillis()+".xls";

         //sheet名
         String sheetName = "女会员信息表";

        int i=0;
        String [][] content = new String[arron.size()][title.length];
        for (Map<String,Object> m: arron) {
            content[i][0] = m.get("phone")+"";
            content[i][1] = m.get("nickName")+"";
            content[i][2] = m.get("age")+"";
            content[i][3] = m.get("tall")+"";
            content[i][4] = m.get("weight")+"";
            content[i][5] = m.get("total")+"";
            content[i][6] = m.get("createTime")+"";
            i++;
        }

            //创建HSSFWorkbook
            HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

            //响应到客户端
            try {
               this.setResponseHeader(response, fileName);
               OutputStream os = response.getOutputStream();
               wb.write(os);
               os.flush();
               os.close();
            } catch (Exception e) {
                   e.printStackTrace();
            }
    }

    @RequestMapping(value = "/orders")
    @ResponseBody
    public void orders(HttpServletRequest request, HttpServletResponse response,String start_time,String end_time,String condition,String state) throws Exception {
        List<String>whereSQL=new ArrayList<>();
        if(!Tool.isNull(condition))whereSQL.add("(sso.nick_name LIKE '%"+condition+"%' OR sso.phone LIKE '%"+condition+"%')");
        if(!Tool.isNull(state))whereSQL.add("`order`.state = '"+state+"'");
        if(!Tool.isNull(start_time))whereSQL.add("`order`.create_time >= '"+start_time+"'");
        if(!Tool.isNull(end_time))whereSQL.add("`order`.finished_time <= '"+end_time+" 23:59:59'");
        List<Map<String,Object>>orders=dao.selectBySQL("SELECT if(sso.id is null,'该用户已被删除',IFNULL(sso.nick_name,'黔城交友用户')) AS nickName\n" +
                "\t, IF(sso.id is null,'该用户已被删除',IFNULL(sso.phone,'0')) AS phone\n"+
                "\t, IF(`order`.state = '1', '已支付', IF(`order`.state = '0', '待支付', '其他')) AS state\n" +
                "\t, concat(`order`.pay_money, '元') AS payMoney\n" +
                "\t, IF(`order`.pay_channel = 'wechat', '微信', IF(`order`.pay_channel = 'ali', '支付宝', '其他')) AS payChannel\n" +
                "\t, `order`.order_from orderFrom\n"+
                "\t, DATE_FORMAT(`order`.pay_time, '%Y-%m-%d %H:%i:%s') AS payTime\n" +
                "\t, DATE_FORMAT(`order`.finished_time, '%Y-%m-%d %H:%i:%s') AS finishedTime\n" +
                "FROM t_order `order`\n" +
                "\tLEFT JOIN t_sso sso ON sso.sso_id = `order`.sso_id\n" +
                (!Tool.listIsNull(whereSQL)?("WHERE "+ StringUtils.join(whereSQL," AND ")+" \n"):"")+
                " order by `order`.create_time ASC");
        orders.parallelStream().forEach(order->{
            if(!Tool.isNull(order.get("orderFrom"))){
                List<String>orderFroms=new ArrayList<>(Arrays.asList(order.get("orderFrom").toString().split(":")));
                order.put("orderFrom",((orderFroms.contains("buyVip")?"购买会员":"")+(orderFroms.size()>3?orderFroms.get(3):"")));
            }
        });
        //excel标题
        String[] title = {"昵称","手机号(账号)","支付状态","实付金额","支付方式","订单内容","支付时间","交易完成时间"};

        //excel文件名
        String fileName = "会员消费记录"+System.currentTimeMillis()+".xls";

        //sheet名
        String sheetName = "会员消费记录";

        int i=0;
        String [][] content = new String[orders.size()][title.length];
        for (Map<String,Object> m: orders) {
            content[i][0] = m.get("nickName")+"";
            content[i][1] = m.get("phone")+"";
            content[i][2] = m.get("state")+"";
            content[i][3] = m.get("payMoney")+"";
            content[i][4] = m.get("orderFrom")+"";
            content[i][5] = m.get("payChannel")+"";
            content[i][6] = m.get("payTime")+"";
            content[i][7] = m.get("finishedTime")+"";
            i++;
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}