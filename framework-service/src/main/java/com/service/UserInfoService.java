package com.service;

import com.dao.UserInfoMongoDao;
import com.entity.UserInfo;
import com.mongodb.MongoGenericService;
import com.mongodb.MongodbBaseDao;
import com.utils.IdGeneratorClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname UserInfoService
 * @Description TODO
 * @Date 2020/1/2 17:39
 * @Created by 125937
 */
@Service
public class UserInfoService  extends MongoGenericService<UserInfo> {

    public UserInfoService(@Autowired UserInfoMongoDao genDao) {
        super(genDao);
    }

    public void saveUser(){
        UserInfo userInfo=new UserInfo();
        userInfo.setAddress("测试一下地址");
        userInfo.setCity("深圳");
        userInfo.setCountry("广东");
        userInfo.setProvince("中国");
        userInfo.setUserName("yxb");
        userInfo.setPassWord("123456");
        userInfo.setId(IdGeneratorClient.getId());
        genDao.insert(userInfo);
    }

}
