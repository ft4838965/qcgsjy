/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: WeChatTest
 * Author:   Arron-Wu
 * Date:     2019/5/19 14:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.stylefeng.guns.util.wechat_public;

import net.sf.json.JSONObject;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Arron
 * @create 2019/5/19
 * @since 1.0.0
 */
public class WxCreateMenu {
    public static void main(String[] args) {
//        AccessToken acessToken = WeChatUtil.getAcessToken();
//        System.out.println("令牌 : "+acessToken.getToken());
//        System.out.println("有效期 ： "+acessToken.getExpiresIn());
        try {

            //获取跟目录
//            File path = null;
//            path = new File(ResourceUtils.getURL("classpath:").getPath());
//            if(!path.exists()) path = new File("");
//
//            //如果上传目录为static/api_img/，则可以如下获取：
//            File upload = new File(path.getAbsolutePath(),"/file");
//            if(!upload.exists()) upload.mkdirs();
//            String filePath =upload.getAbsolutePath();
//
//            ObjectOutputStream objectOutputStream=new ObjectOutputStream(new FileOutputStream("D://access_token"));
//            objectOutputStream.writeObject(acessToken);
//            objectOutputStream.close();
            //对象输入流
            ObjectInputStream objectInputStream=new ObjectInputStream(new FileInputStream(new File("D://access_token")));
            AccessToken tempQDingVo= (AccessToken) objectInputStream.readObject();
            System.out.println("acessToken:"+tempQDingVo.getToken()+" : "+tempQDingVo.getExpiresIn());
            objectInputStream.close();

            String menu = JSONObject.fromObject(WeChatUtil.initMenu()).toString();
            int result = WeChatUtil.createMenu(tempQDingVo.getToken(), menu);
            if (result == 0){
                System.out.println("ok");
            }else {
                System.out.println("错误码 ："+result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}