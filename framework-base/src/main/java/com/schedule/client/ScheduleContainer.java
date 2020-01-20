package com.schedule.client;

import com.annotations.ScheduleJob;
import com.entity.ScheduleInfo;
import com.schedule.manager.QuartzManager;
import com.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname ScheduleContainer
 * @Description 调度任务容器
 * @Date 2020/1/8 11:35
 * @Created by 125937
 */
@Component
@Order(2)
@Slf4j
public class ScheduleContainer implements ApplicationContextAware, ApplicationRunner {

    @Value("${spring.application.name}")
    private String serviceName;
    private static final Map<String,ScheduleJobHandler> scheduleJobHandlerMap=new ConcurrentHashMap<>();
    private static final Map<String,ScheduleInfo> scheduleInfos=new ConcurrentHashMap<>();

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       String[]  jobHandlers= applicationContext.getBeanNamesForType(ScheduleJobHandler.class);
       if(jobHandlers==null || jobHandlers.length<1){
           log.info("ScheduleContainer client is null");
           return;
       }
       for(String beanName : jobHandlers){
           ScheduleJobHandler jobHandler= applicationContext.getBean(beanName,ScheduleJobHandler.class);
           Class<?> targetClass=AopUtils.getTargetClass(jobHandler);
           ScheduleJob scheduleJob= AnnotationUtils.findAnnotation(targetClass,ScheduleJob.class);
           Assert.notNull(jobHandler,"ScheduleContainer ScheduleJobHandler scheduleJob not null "+targetClass.getName());
           String packageName = targetClass.getPackage().getName();
           String className = targetClass.getSimpleName();
           String taskName = String.format("%s_%s_%s", this.serviceName, packageName, className);
           scheduleJobHandlerMap.put(taskName,jobHandler);

           String cronCondition = scheduleJob.cronCondition();
           String executeParam = scheduleJob.executeParam();
           String taskDesc = scheduleJob.taskDesc();
           Assert.hasText(cronCondition, "cronCondition required");

           ScheduleInfo scheduleInfo=new ScheduleInfo();
           scheduleInfo.setCronCondition(cronCondition);
           scheduleInfo.setExecuteParam(executeParam);
           scheduleInfo.setServiceName(serviceName);
           scheduleInfo.setTaskDesc(taskDesc);
           scheduleInfo.setTaskGroup(serviceName);
           scheduleInfo.setTaskState("启用");
           scheduleInfo.setTaskName(taskName);
           scheduleInfos.put(taskName,scheduleInfo);
       }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for(Map.Entry<String,ScheduleInfo> entry : scheduleInfos.entrySet()) {
            Criteria criteria = Criteria.where("taskName").is(entry.getKey());
            Query query = new Query(criteria);
            List<ScheduleInfo> dbInfos = scheduleService.find(query);
            if (CollectionUtils.isEmpty(dbInfos)) {
                scheduleService.insert(entry.getValue());
                //添加调度任务
                QuartzManager.addJob(entry.getValue());
            } else {
                ScheduleInfo scheduleInfo=entry.getValue();
                ScheduleInfo dbScheduleInfo=dbInfos.get(0);
                scheduleInfo.setId(dbScheduleInfo.getId());
                scheduleInfo.setCreateBy(dbScheduleInfo.getCreateBy());
                scheduleInfo.setCreateTime(dbScheduleInfo.getCreateTime());
                scheduleService.update(scheduleInfo);
                if(dbInfos.get(0).getTaskState().equals("启用")){
                    //添加调度任务
                    QuartzManager.addJob(entry.getValue());
                }
            }
            log.info("ScheduleJobHandler taskName register success:{}",entry.getKey());
        }
    }

    public static ScheduleJobHandler getScheduleJobHandler(String taskName){
        return scheduleJobHandlerMap.get(taskName);
    }
}
