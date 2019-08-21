/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: WxLogin
 * Author:   Arron-Wu
 * Date:     2019/4/24 22:12
 * Description: 微信登录接口
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.stylefeng.guns.util.authUtil;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.awt.SunHints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

/**
 * 〈一句话功能简述〉<br> 
 * 〈微信登录接口〉
 *
 * @author Arron
 * @create 2019/4/24
 * @since 1.0.0
 */
@Controller
@RequestMapping("/authLogin")
public class AuthLogin {

    @RequestMapping("/wxLogin")
    public void wxLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String backUrl = "http://24621v53n0.wicp.vip/authLogin/wxCallBack";
    String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +AuthUtil.WXAPPID +
            "&redirect_uri=" + URLEncoder.encode(backUrl) +
            "&response_type=code" +
            "&scope=SCOPE" +
            "&state=STATE#wechat_redirect";
    response.sendRedirect(url);
    }

    @RequestMapping(value = "wxCallBack", method = RequestMethod.GET)
    public void wxCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String url = " https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID" + AuthUtil.WXAPPID +
                "&secret=" + AuthUtil.WXAPPSECRET +
                "&code=" + code +
                "&grant_type=authorization_code";
        JSONObject jsonObject = AuthUtil.doGetJson(url);
        String openid = jsonObject.getString("openid");
        String token = jsonObject.getString("access_token");
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token +
                "&openid=" + openid +
                "&lang=zh_CN";
        JSONObject userInfo = AuthUtil.doGetJson(infoUrl);
        System.out.println(userInfo);

    }
}