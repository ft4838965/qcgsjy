package com.stylefeng.guns.task;

import com.stylefeng.guns.modular.setting.service.ISettingService;
import com.stylefeng.guns.modular.system.model.Setting;
import com.stylefeng.guns.util.Tool;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Heyifan Cotter on 2019/1/22.
 */
@Component //使spring管理
@EnableScheduling
public class Jobs implements ApplicationListener<WebServerInitializedEvent> {
    public Jobs(){
        System.err.println("123");
    }
    @Autowired
    private ISettingService settingService;
    /**
     * 定时获取access_token和ticket
     */
    @Scheduled(initialDelay=1000,fixedRate = 7140000)
    public void test() {
//        if(getPort()==8081)return;
        System.err.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        try{
            CloseableHttpClient client = HttpClients.createDefault();
            String access_token="",jsapi_ticket="";
            Setting setting=settingService.selectById(1);
            String responseText = "";
            CloseableHttpResponse response = null;
            HttpGet method = new HttpGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+setting.getWechatAppId()+"&secret="+setting.getWechatAppSecret());
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity, "UTF-8");
                Map<String,Object> responseMap= JSONObject.fromObject(responseText);
                System.err.println(responseMap);
                if(!Tool.isNull(responseMap.get("access_token"))){
                    access_token=responseMap.get("access_token").toString();
                    CloseableHttpClient client1 = HttpClients.createDefault();
                    String responseText1 = "";
                    CloseableHttpResponse response1 = null;
                    HttpGet method1 = new HttpGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi");
                    response1 = client1.execute(method1);
                    HttpEntity entity1 = response1.getEntity();
                    if (entity1 != null) {
                        responseText1 = EntityUtils.toString(entity1, "UTF-8");
                        Map<String,Object> responseMap1= JSONObject.fromObject(responseText1);
                        System.err.println(responseMap1);
                        if(!Tool.isNull(responseMap1.get("errcode"))&&Integer.parseInt(responseMap1.get("errcode").toString())==0
                        &&!Tool.isNull(responseMap1.get("errmsg"))&&"ok".equals(responseMap1.get("errmsg").toString())
                        &&!Tool.isNull(responseMap1.get("ticket"))){
                            jsapi_ticket=responseMap1.get("ticket").toString();
                            setting.setWechatAccessToken(access_token);
                            setting.setWechatTicket(jsapi_ticket);
                            settingService.updateAllColumnById(setting);
                        }
                    }
                    response1.close();
                    client1.close();
                }
            }
            response.close();
            client.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private int serverPort;

    public int getPort() {
        return this.serverPort;
    }
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
        System.err.println(serverPort);
    }
}
