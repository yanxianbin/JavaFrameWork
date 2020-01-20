package com.schedule.manager;

import com.entity.ScheduleInfo;
import com.utils.CommonUtils;
import com.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerKey.triggerKey;

/**
 * @Classname QuartzManager
 * @Description 任务管理器
 * @Date 2020/1/8 15:39
 * @Created by 125937
 */
@Slf4j
public class QuartzManager {

    /**
     * 调度器
     */
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();

    /**
     * 添加一个定时任务
     * @param scheduleInfo
     */
    public static void addJob(ScheduleInfo scheduleInfo) {
        try {
            //通过SchedulerFactory来获取一个调度器
            Scheduler sched = gSchedulerFactory.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(JobExecuteTarget.class)
                    .withIdentity(scheduleInfo.getTaskName(), scheduleInfo.getTaskGroup())
                    .withDescription(scheduleInfo.getTaskDesc())
                    .usingJobData("param", JsonUtils.deSerializable(scheduleInfo)).build();
            //触发器
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey(scheduleInfo.getTaskName(), scheduleInfo.getTaskGroup()))
                    .withSchedule(cronSchedule(scheduleInfo.getCronCondition()))
                    .build();

            sched.scheduleJob(jobDetail, cronTrigger);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
           log.error("addJob error:{}", CommonUtils.getExceptionMsg(e),e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新定时任务
     * @param scheduleInfo
     * @throws SchedulerException
     */
    public static void updateJob(ScheduleInfo scheduleInfo) throws SchedulerException {
        JobKey jobKey= JobKey.jobKey(scheduleInfo.getTaskName(), scheduleInfo.getTaskGroup());
        gSchedulerFactory.getScheduler().deleteJob(jobKey);
        addJob(scheduleInfo);
    }

    /**
     * 暂停任务
     * @param scheduleInfo
     * @throws SchedulerException
     */
    public static void pauseJob(ScheduleInfo scheduleInfo) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleInfo.getTaskName(), scheduleInfo.getTaskGroup());
        gSchedulerFactory.getScheduler().pauseJob(jobKey);
    }

    /**
     * 恢复任务
     * @param scheduleInfo
     * @throws SchedulerException
     */
    public static void resumeJob(ScheduleInfo scheduleInfo) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleInfo.getTaskName(), scheduleInfo.getTaskGroup());
        gSchedulerFactory.getScheduler().resumeJob(jobKey);
    }

    /**
     * 马上运行
     * @param scheduleInfo
     * @throws SchedulerException
     */
    public static void runJobNow(ScheduleInfo scheduleInfo) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleInfo.getTaskName(), scheduleInfo.getTaskGroup());
        gSchedulerFactory.getScheduler().triggerJob(jobKey);
    }
}
