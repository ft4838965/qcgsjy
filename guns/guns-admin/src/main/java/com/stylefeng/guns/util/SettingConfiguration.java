package com.stylefeng.guns.util;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.util.quartz.MyJob2;
import com.stylefeng.guns.util.quartz.QuartzUtils;
import com.stylefeng.guns.modular.setting.service.ISettingService;
import com.stylefeng.guns.modular.system.model.Setting;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SettingConfiguration {

    @Autowired
    private ISettingService settingService;

    @Autowired
    SchedulerFactoryBean factoryBean;

    @Bean
    public Setting getSetting(){
        // 每天凌晨3点执行VIP减天数任务
        Scheduler scheduler = factoryBean.getScheduler();
        QuartzUtils.addJob(scheduler, "deVipDay", MyJob2.class, null, "0 0 3 * * ?");

        Setting setting=new Setting();
        setting.setId(1);
        setting=settingService.selectOne(new EntityWrapper<>(setting));
        System.err.println("------------------");
        System.err.println("---加载系统配置---");
        System.err.println("------------------");
        return setting==null?new Setting():setting;
    }
}
