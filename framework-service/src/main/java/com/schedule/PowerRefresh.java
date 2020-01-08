package com.schedule;

import com.annotations.ScheduleJob;
import com.entity.ScheduleInfo;
import com.schedule.client.ScheduleJobHandler;
import com.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @Classname PowerRefresh
 * @Description 权限信息定时刷新任务
 * @Date 2020/1/8 11:59
 * @Created by 125937
 */
@ScheduleJob(taskDesc = "权限信息定时刷新任务",executeParam = "{权限信息定时刷新任务}",cronCondition = "0 0/1 * * * ?")
@Slf4j
public class PowerRefresh implements ScheduleJobHandler {
    @Override
    public void execute(Long logId, ScheduleInfo scheduleInfo) {
        log.info("PowerRefresh message:{}", JsonUtils.deSerializable(scheduleInfo));
    }
}
