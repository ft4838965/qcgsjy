package com.stylefeng.guns.util.QueueUtil;

import com.stylefeng.guns.modular.system.dao.Dao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")//spring 多例
public class BusinessThread implements Runnable {
    private Dao dao;
    private String acceptStr;

    public BusinessThread(String acceptStr, Dao dao) {
        this.acceptStr = acceptStr;
        this.dao = dao;
    }
    public BusinessThread(){};
    public Dao getDao(){return dao;}
    public String getAcceptStr() {
        return acceptStr;
    }
    public void setDao(Dao dao){this.dao=dao;}
    public void setAcceptStr(String acceptStr) {
        this.acceptStr = acceptStr;
    }
    @Override
    public void run() {
        //业务操作
//        System.out.println("多线程已经处理系统消息update的SQL语句："+acceptStr);
//        System.err.println(dao.selectBySQL(acceptStr));
        //线程阻塞
        try {
            Thread.sleep(1000);
//            System.out.println("多线程已经处理系统消息update的SQL语句："+acceptStr);
//            System.err.println(dao.selectBySQL(acceptStr));
            dao.updateBySQL(acceptStr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
