package com.service;

import com.entity.Admin;
import com.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname AdminService
 * @Description 管理员服务类
 * @Date 2020/5/27 16:39
 * @Created by 125937
 */
@Service
public class AdminService extends GenericService<Admin> {
    public AdminService(@Autowired AdminMapper mapper) {
        super(mapper);
    }

    public AdminMapper getMapper(){
        return (AdminMapper)super.baseMapper;
    }

    public List<Admin> selectAll(){
      List<Admin> list= selectList(new Admin());
      return list;
    }
}
