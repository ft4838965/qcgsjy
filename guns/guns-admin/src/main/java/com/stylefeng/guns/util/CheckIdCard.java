/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: CheckIdCard
 * Author:   Arron-Wu
 * Date:     2019/3/2 16:42
 * Description: 实名认证
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.stylefeng.guns.util;

import com.stylefeng.guns.modular.system.model.Setting;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈实名认证〉
 *
 * @author Arron
 * @create 2019/3/2
 * @since 1.0.0
 */
public class CheckIdCard {
    public static String check(String idCard, String name) {
        try {
            String host = "https://idcert.market.alicloudapi.com";
            String path = "/idcard";
            String method = "GET";
            String appcode = "eb705ddfa6134fdaa29192eb2bc228d8";
            Map<String, String> headers = new HashMap<String, String>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + appcode);
            Map<String, String> querys = new HashMap<String, String>();
            querys.put("idCard", idCard);
            querys.put("name", name);
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            //System.out.println(response.toString());如不输出json, 请打开这行代码，打印调试头部状态码。
            //状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
            //获取response的body
            String responseEntity = EntityUtils.toString(response.getEntity());
            JSONObject resultObject = resultObject = JSONObject.fromObject(!Tool.isNull(responseEntity) && responseEntity.startsWith("{") && responseEntity.endsWith("}") ? responseEntity : "{}");
            switch (response.getStatusLine().getStatusCode()) {
                case 400:
                    return "URL无效";
                case 401:
                    return "appCode错误,请联系管理员修改后台";
                case 403:
                    return "实名认证次数用完";
                case 500:
                    return "第三方实名认证接口异常";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "系统错误";
        }
        return "验证成功";
    }

}