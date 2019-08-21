/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: AuthUtil
 * Author:   Arron-Wu
 * Date:     2019/4/24 21:55
 * Description: 三方登录工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.stylefeng.guns.util.authUtil;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈三方登录工具类〉
 *
 * @author Arron
 * @create 2019/4/24
 * @since 1.0.0
 */
public class AuthUtil {

    public static final String WXAPPID = "wxc3d69f9fac93c815";
    public static final String WXAPPSECRET = "401dd1c907ae4614c27fc298e73df04d";

    public static JSONObject doGetJson(String url) throws IOException {
        JSONObject jsonObject = null;
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null){
            String result = EntityUtils.toString(entity, "UTF-8");
            JSONObject.fromObject(result);
        }
        httpGet.releaseConnection();
        return jsonObject;
    }
}