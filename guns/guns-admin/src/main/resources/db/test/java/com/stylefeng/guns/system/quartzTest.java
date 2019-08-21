/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: quartzTest
 * Author:   Arron-Wu
 * Date:     2019/3/12 23:18
 * Description: 测试quartz
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
/*
package com.stylefeng.guns.system;

import com.stylefeng.guns.base.BaseJunit;
import com.stylefeng.guns.util.quartz.MyJob;
import com.stylefeng.guns.util.quartz.MyJob2;
import com.stylefeng.guns.util.quartz.QuartzUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

*/
/**
 * 〈一句话功能简述〉<br>
 * 〈测试quartz〉
 *
 * @author Arron
 * @create 2019/3/12
 * @since 1.0.0
 *//*

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:quartz.xml")
public class quartzTest{
    @Autowired
    SchedulerFactoryBean factoryBean;

    @Test
    public void quartzTest() throws Exception{
        Scheduler scheduler = factoryBean.getScheduler();
        QuartzUtils.addJob(scheduler, "easybuy", MyJob.class, null, "0/1 * * * * ?");
        Thread.sleep(10*1000);
        QuartzUtils.removeJob(scheduler, "easybuy");
    }

    @Test
    public void quartzTest2() throws Exception{
        Scheduler scheduler = factoryBean.getScheduler();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderId", "1234234232445");
        hashMap.put("name", "你喜欢的凤姐");
        QuartzUtils.addJob(scheduler, "easybuy", MyJob2.class, hashMap, "0/5 * * * * ?");
        Thread.sleep(10*1000);
        QuartzUtils.removeJob(scheduler, "easybuy");
    }

    @Test
    public void quartzTest3() throws Exception{
        Scheduler scheduler = factoryBean.getScheduler();
        QuartzUtils.addJob(scheduler, "easybuy1", MyJob2.class, "preHandle", "0/1 * * * * ?");
        QuartzUtils.addJob(scheduler, "easybuy2", MyJob2.class, "beginHandle", "0/1 * * * * ?");
        Thread.sleep(10*1000);
        QuartzUtils.removeJob(scheduler, "easybuy1");
        QuartzUtils.removeJob(scheduler, "easybuy2");
    }
}*/
