package com.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public interface UserInfoService {

    @RequestMapping("/isAdminUser")
    Boolean isAdminUser(@RequestParam("userName")String userName);
}
