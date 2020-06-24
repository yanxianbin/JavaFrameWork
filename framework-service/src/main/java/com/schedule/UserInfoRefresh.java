package com.schedule;

import com.annotations.ScheduleJob;
import com.entity.ScheduleInfo;
import com.schedule.client.ScheduleJobHandler;
import com.service.eventbus.PowerChangeEvent;
import com.service.eventbus.UserInfoChangeEvent;
import com.utils.JsonUtils;
import com.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @Classname UserInfoRefresh
 * @Description 用户信息定时刷新任务
 * @Date 2020/1/8 11:59
 * @Created by 125937
 */
@ScheduleJob(taskDesc = "用户信息定时刷新任务",executeParam = "{ddfdfd}",cronCondition = "0 0 0/1 * * ?")
@Slf4j
public class UserInfoRefresh implements ScheduleJobHandler {
    @Override
    public void execute(Long logId, ScheduleInfo scheduleInfo) {
        //发布事件
        SpringUtils.publisherEvent(new UserInfoChangeEvent(scheduleInfo));
        SpringUtils.publisherEvent(new PowerChangeEvent(scheduleInfo));
        log.info("UserInfoRefresh message:{}", JsonUtils.deSerializable(scheduleInfo));
    }
}
