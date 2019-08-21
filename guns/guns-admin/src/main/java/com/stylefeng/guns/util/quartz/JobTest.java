package com.stylefeng.guns.util.quartz;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Heyifan Cotter on 2019/3/13.
 */
@Configuration
public class JobTest {

    @Bean
    public JobDetail teatQuartzDetail(){
        return JobBuilder.newJob(MyJob.class).withIdentity("jobTest").storeDurably().build();
    }

    @Bean
    public Trigger testQuartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(12*60*60)  //设置时间周期单位秒  12小时
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(teatQuartzDetail())
                .withIdentity("jobTest")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
