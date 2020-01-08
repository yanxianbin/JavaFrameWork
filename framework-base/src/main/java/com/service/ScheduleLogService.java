package com.service;

import com.entity.ScheduleLog;
import com.mongodb.MongoGenericService;
import com.mongodb.MongodbBaseDao;
import org.springframework.stereotype.Service;

/**
 * @Classname ScheduleLogService
 * @Description TODO
 * @Date 2020/1/8 16:41
 * @Created by 125937
 */
@Service
public class ScheduleLogService extends MongoGenericService<ScheduleLog> {
    public ScheduleLogService(MongodbBaseDao<ScheduleLog> genDaoMap) {
        super(genDaoMap);
    }
}
