package com.controller;

import com.entity.Admin;
import com.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Classname AdminController
 * @Description TODO
 * @Date 2020/5/28 11:36
 * @Created by 125937
 */
@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/admin/list",method = RequestMethod.GET)
    @ResponseBody
    public List<Admin> adminList(){
        List<Admin> list= adminService.selectAll();
        list.addAll(adminService.getMapper().testSelect());
        return list;
    }
}
