package com.service;

import com.entity.ScheduleInfo;
import com.mongodb.MongoGenericService;
import com.mongodb.MongodbBaseDao;
import org.springframework.stereotype.Service;

/**
 * @Classname ScheduleService
 * @Description 调度任务服务类
 * @Date 2020/1/8 11:15
 * @Created by 125937
 */
@Service
public class ScheduleService extends MongoGenericService<ScheduleInfo> {

    public ScheduleService(MongodbBaseDao<ScheduleInfo> genDaoMap) {
        super(genDaoMap);
    }
}
