package com.messageframe.service;

import com.messageframe.mode.ReceiveMode;
import com.mongodb.MongodbBaseDao;
import org.springframework.stereotype.Repository;

/**
 * @Classname ReceiveMessageDao
 * @Description TODO
 * @Date 2020/1/3 14:26
 * @Created by 125937
 */
@Repository
public class ReceiveMessageDao extends MongodbBaseDao<ReceiveMode> {
}
