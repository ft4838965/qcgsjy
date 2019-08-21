/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: RedisService
 * Author:   Arron-Wu
 * Date:     2019/4/28 21:56
 * Description: redis工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.stylefeng.guns.util.redis;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈redis工具类〉
 *
 * @author Arron
 * @create 2019/4/28
 * @since 1.0.0
 */
@Service
public class RedisService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public <T> boolean set(String key ,T value){

        try {

            //任意类型转换成String
            String val = beanToString(value);

            if(val==null||val.length()<=0){
                return false;
            }

            stringRedisTemplate.opsForValue().set(key,val);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public <T> T get(String key,Class<T> clazz){
        try {
            String value = stringRedisTemplate.opsForValue().get(key);

            return stringToBean(value,clazz);
        }catch (Exception e){
            return null ;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T stringToBean(String value, Class<T> clazz) {
        if(value==null||value.length()<=0||clazz==null){
            return null;
        }

        if(clazz ==int.class ||clazz==Integer.class){
            return (T)Integer.valueOf(value);
        }
        else if(clazz==long.class||clazz==Long.class){
            return (T)Long.valueOf(value);
        }
        else if(clazz==String.class){
            return (T)value;
        }else {
            return JSON.toJavaObject(JSON.parseObject(value),clazz);
        }
    }

    /**
     *
     * @param Every T values
     * @param T 任意类型
     * @return String
     */
    private <T> String beanToString(T value) {

        if(value==null){
            return null;
        }
        Class <?> clazz = value.getClass();
        if(clazz==int.class||clazz==Integer.class){
            return ""+value;
        }
        else if(clazz==long.class||clazz==Long.class){
            return ""+value;
        }
        else if(clazz==String.class){
            return (String)value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * 处理数组JSON
     *
     */
    public void handleJSONArray(String JSONValue, List<Map<String,Object>> list) {
        com.alibaba.fastjson.JSONArray m =  JSON.parseArray(JSONValue);
        for (int i = 0; i < m.size(); i++) {
            com.alibaba.fastjson.JSONObject jsonObject = m.getJSONObject(i);
            Map map1 = (Map) JSONObject.toBean(JSONObject.fromObject(jsonObject), Map.class);
            list.add(map1);
        }
    }
}
