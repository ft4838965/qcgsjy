package com.stylefeng.guns.core.intercept;

import com.stylefeng.guns.core.cache.CacheKit;
import com.stylefeng.guns.modular.system.dao.Dao;
import com.stylefeng.guns.util.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Heyifan Cotter on 2019/2/20.
 */
@Component
public class identify_Intercepter implements HandlerInterceptor {

    @Autowired
    private Dao dao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.putAll(request.getParameterMap());
        try{
            if(map.containsKey("token")){
                String token = map.get("token")+"";
                String constant = CacheKit.get("CONSTANT", token)+"";
                if(Tool.isNull(constant)){
                    //鉴权
                    List<Map<String, Object>> maps = dao.selectBySQL("select token from t_sso where token = " + "'" + token + "'");
                    if (Tool.listIsNull(maps)){
                        return false;
                    }else {
                        CacheKit.put("CONSTANT",token,token);
                    }
                }else if(!constant.equals(token)){
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }
}
