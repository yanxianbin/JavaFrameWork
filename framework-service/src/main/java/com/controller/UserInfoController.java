package com.controller;

import org.springframework.web.bind.annotation.RestController;
import com.service.UserInfoService;

@RestController
public class UserInfoController implements UserInfoService {
    @Override
    public Boolean isAdminUser(String userName) {
        return true;
    }
}
