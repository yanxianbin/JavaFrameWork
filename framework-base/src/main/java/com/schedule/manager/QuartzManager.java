package com.schedule.manager;

import com.entity.ScheduleInfo;
import com.utils.JsonUtils;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname QuartzManager
 * @Description 任务管理器
 * @Date 2020/1/8 15:39
 * @Created by 125937
 */
public class QuartzManager {
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();

    /**
     * 添加一个定时任务
     * @param scheduleInfo
     */
    public static void addJob(ScheduleInfo scheduleInfo) {
        try {
            //通过SchedulerFactory来获取一个调度器
            Scheduler sched = gSchedulerFactory.getScheduler();
            JobDetailImpl job=new JobDetailImpl();
            job.setGroup(scheduleInfo.getTaskGroup());
            job.setName(scheduleInfo.getTaskName());
            job.setDescription(scheduleInfo.getTaskDesc());
            JobKey jobKey=new JobKey(scheduleInfo.getTaskName());
            job.setKey(jobKey);
            job.setJobClass(JobExecuteTarget.class);
            Map<String,String> paramMap=new HashMap<>();
            paramMap.put("param", JsonUtils.deSerializable(scheduleInfo));
            JobDataMap jobDataMap=new JobDataMap(paramMap);
            job.setJobDataMap(jobDataMap);

            //触发器
            CronTriggerImpl trigger = new CronTriggerImpl();
            trigger.setCronExpression(scheduleInfo.getCronCondition());
            trigger.setName(scheduleInfo.getTaskName());
            sched.scheduleJob(job,trigger);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
