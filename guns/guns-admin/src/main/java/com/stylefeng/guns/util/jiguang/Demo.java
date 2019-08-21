package com.stylefeng.guns.util.jiguang;

import java.util.HashMap;

/**
 * Created by Arron Cotter on 2019/3/29.
 * 极光测试
 */
public class Demo {

        public static void main(String[] args) {

            HashMap<String, String > extras = new HashMap<>();
            extras.put("modle","friend_circle");
            extras.put("type","like");
            extras.put("id","1");

            //单发
/*            ArrayList<String> alias = new ArrayList<>();
            alias.add("194");
            if (JpushClientUtil.sendToAlias(alias,"赛事活动", "", "这TM是怎么回事","","", extras)==1) System.out.println("success");
            else System.out.println("fail");*/

            //群发
            if(JpushClientUtil.sendToAll("星厨上门","","您预订的 4月30日 的星厨上门服务订单下单成功，将与您取得联系，请保持手机畅通。", "", "", extras) == 1){
              System.out.println("success");
            }else System.out.println("fail");

        }

}
