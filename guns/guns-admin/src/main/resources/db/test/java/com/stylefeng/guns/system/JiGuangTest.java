package com.stylefeng.guns.system;

import com.stylefeng.guns.util.JiGuangUtil;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Heyifan Cotter on 2019/3/18.
 */
public class JiGuangTest {
    private static final Logger log = LoggerFactory.getLogger(JiGuangTest.class);
    @Test
    public void jiGuangTest(){
        String masterSecret = "edc155db81b06ea425a2975c";
        String appKey = "2d420a33198e1e5791a7750a";
        String pushUrl = "https://api.jpush.cn/v3/push";
        boolean apns_production = true;
        int time_to_live = 86400;
        final String ALERT = "推送信息";
        String alias = "192.168.1.20";//别名（一个别名对应一个机器设备或多个，可以用用户的ID来唯一识别）
        try{
            String result = JiGuangUtil.push(pushUrl,alias,ALERT,appKey,masterSecret,apns_production,time_to_live);
            JSONObject resData = JSONObject.fromObject(result);
            if(resData.containsKey("error")){
                log.info("针对别名为" + alias + "的信息推送失败！");
                JSONObject error = JSONObject.fromObject(resData.get("error"));
                log.info("错误信息为：" + error.get("message").toString());
            }
            log.info("针对别名为" + alias + "的信息推送成功！");
        }catch(Exception e){
            log.error("针对别名为" + alias + "的信息推送失败！",e);
        }

        }
    }
