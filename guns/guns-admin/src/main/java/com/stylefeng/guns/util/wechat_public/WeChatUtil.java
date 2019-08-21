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
package com.stylefeng.guns.util.wechat_public;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
public class WeChatUtil {

    public static final String WXAPPID = "wxf74eceec3b13ef22";
    public static final String WXAPPSECRET = "fc755599f2ba0e9dfd068201189e703f";

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    /**
     * GET
     * @param url
     * @return
     * @throws IOException
     */
    public static JSONObject doGetJson(String url)  {
        JSONObject jsonObject = null;
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null){
                String result = EntityUtils.toString(entity, "UTF-8");
                jsonObject =JSONObject.fromObject(result);
            }
            httpGet.releaseConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * POST
     * @param url
     * @param outStr
     * @return
     * @throws IOException
     */
    public static JSONObject doPostJson(String url,String outStr) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = null;
        httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
        HttpResponse response = null;
        try {
            response = client.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(),"UTF-8");
            jsonObject = JSONObject.fromObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 获取access_token
     * @return
     */
    public static AccessToken getAcessToken(){
        AccessToken token = new AccessToken();
        String url = ACCESS_TOKEN_URL.replace("APPID", WXAPPID).replace("APPSECRET", WXAPPSECRET);
        JSONObject jsonObject = doGetJson(url);
        if (jsonObject!=null){
            token.setToken(jsonObject.getString("access_token"));
            token.setExpiresIn(jsonObject.getInt("expires_in"));
        }
        return token;
    }

    /**
     * 组装菜单
     * @return
     */
    public static Menu initMenu(){
        Menu menu = new Menu();
        ClickButton clickButton = new ClickButton();
        clickButton.setKey("1");
        clickButton.setName("click菜单");
        clickButton.setType("click");

        ViewButton viewButton = new ViewButton();
        viewButton.setName("view菜单");
        viewButton.setType("view");
        viewButton.setUrl("http://www.baidu.com");

        ClickButton clickButton1 = new ClickButton();
        clickButton1.setKey("2");
        clickButton1.setName("扫码事件");
        clickButton1.setType("scancode_push");

        ClickButton clickButton2 = new ClickButton();
        clickButton2.setKey("3");
        clickButton2.setName("地理位置");
        clickButton2.setType("location_select");

        //子菜单
        Button button = new Button();
        button.setName("菜单");
        button.setSub_button(new Button[]{clickButton1,clickButton2});

        //主菜单
        menu.setButton(new Button[]{clickButton,viewButton,button});

        return menu;
    }

    /**
     * 创建菜单
     * @param token
     * @param menu
     * @return
     */
    public static int createMenu(String token,String menu){
        int result = 0;
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doPostJson(url, menu);
        if (jsonObject!=null){
            result = jsonObject.getInt("errcode");
        }
        return result;
    }
}