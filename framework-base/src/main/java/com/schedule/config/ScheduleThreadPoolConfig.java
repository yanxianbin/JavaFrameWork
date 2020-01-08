package com.schedule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;


/**
 * @Classname ScheduleThreadPoolConfig
 * @Description 任务调度线程池
 * @Date 2020/1/8 14:54
 * @Created by 125937
 */
@Configuration
public class ScheduleThreadPoolConfig {
    @Value("${schedule.client.taskThreadPool.corePoolSize:5}")
    private int corePoolSize;
    @Value("${schedule.client.taskThreadPool.maxPoolSize:10}")
    private int maxPoolSize;
    @Value("${schedule.client.taskThreadPool.keepAliveSeconds:60}")
    private int keepAliveSeconds;
    @Value("${schedule.client.taskThreadPool.queueCapacity:100}")
    private int queueCapacity;
    @Value("${schedule.client.taskThreadPool.allowCoreThreadTimeOut:false}")
    private boolean allowCoreThreadTimeOut;

    public ScheduleThreadPoolConfig() {
    }

    @Bean(name = "scheduleThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
      ThreadPoolTaskExecutor threadPoolTaskExecutor=new ThreadPoolTaskExecutor();
      threadPoolTaskExecutor.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);
      threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
      threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);
      threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
      threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
      return threadPoolTaskExecutor;
    }

    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
