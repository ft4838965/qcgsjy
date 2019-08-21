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
     * 导出报表
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