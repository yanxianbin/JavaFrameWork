package com.dao;

import com.entity.UserInfo;
import com.mongodb.MongodbBaseDao;
import org.springframework.stereotype.Repository;

/**
 * @Classname UserInfoMongoDao
 * @Description TODO
 * @Date 2020/1/2 18:41
 * @Created by 125937
 */
@Repository
public class UserInfoMongoDao extends MongodbBaseDao<UserInfo> {
}
