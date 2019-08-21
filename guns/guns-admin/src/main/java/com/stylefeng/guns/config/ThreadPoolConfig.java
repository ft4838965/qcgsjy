/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: ThreadPoolConfig
 * Author:   Arron-Wu
 * Date:     2019/3/3 22:30
 * Description: 线程池配置
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.stylefeng.guns.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 〈一句话功能简述〉<br> 
 * 〈线程池配置〉
 *
 * @author Arron
 * @create 2019/3/3
 * @since 1.0.0
 */

@Configuration
@EnableAsync
public class ThreadPoolConfig {

        @Bean
        public TaskExecutor taskExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            // 设置核心线程数
            executor.setCorePoolSize(5);
            // 设置最大线程数
            executor.setMaxPoolSize(10);
            // 设置队列容量
            executor.setQueueCapacity(20);
            // 设置线程活跃时间（秒）
            executor.setKeepAliveSeconds(60);
            // 设置默认线程名称
            executor.setThreadNamePrefix("hello-");
            // 设置拒绝策略
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            // 等待所有任务结束后再关闭线程池
            executor.setWaitForTasksToCompleteOnShutdown(true);
            return executor;
        }

}