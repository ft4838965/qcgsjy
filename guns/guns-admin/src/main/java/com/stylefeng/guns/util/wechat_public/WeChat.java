/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: WeChat
 * Author:   Arron-Wu
 * Date:     2019/5/19 10:47
 * Description: 微信认证
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.stylefeng.guns.util.wechat_public;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈微信认证〉
 *
 * @author Arron
 * @create 2019/5/19
 * @since 1.0.0
 */
@Controller
@RequestMapping("/weChat")
public class WeChat {
    private static final String token = "fuck";

    @RequestMapping("/identify")
    public void identify(String signature, String timestamp, String nonce, String echostr, HttpServletResponse response) throws IOException {
        String[] arr = new String[]{token, timestamp, nonce};
        //排序
        Arrays.sort(arr);
        //生成字符串
        StringBuffer content = new StringBuffer();
        Arrays.stream(arr).forEach(m -> {
            content.append(m);
        });
        //sha1加密
        String temp = getSha1(content.toString());

        PrintWriter writer = response.getWriter();
        //校验
        if (temp.equals(signature)){
            writer.println(echostr);
        }

    }

    @RequestMapping(value = "/identify",method = RequestMethod.POST)
    public void sendMessage(HttpServletRequest re,HttpServletResponse response) throws IOException, DocumentException {
        re.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String str = null;
        Map<String,String> map = MessageUtil.xmlToMap(re);

        //从集合中，获取XML各个节点的内容

        String ToUserName = map.get("ToUserName");

        String FromUserName = map.get("FromUserName");

        String CreateTime = map.get("CreateTime");

        String MsgType = map.get("MsgType");

        String Content = map.get("Content");

        String MsgId = map.get("MsgId");

        if(MsgType.equals(MessageUtil.MESSAGE_TEXT)) {//判断消息类型是否是文本消息(text)
          str = MessageUtil.initText(ToUserName,FromUserName,Content);
          //如果是菜单回复，在这里写逻辑
            if("1".equals(Content)){
                //写逻辑
            }else if ("2".equals(Content)){
                //。。。
            }
        }else if (MessageUtil.MESSAGE_EVENT.equals(MsgType)){
            String eventType = map.get("Event");
            if (MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
                str = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.menuText());
            }
        }
            out.print(str); //返回转换后的XML字符串
            out.close();
    }



    /**
     * sha1加密算法
     * @param str
     * @return
     */

    public static String getSha1(String str) {
        if (null == str || 0 == str.length()) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}