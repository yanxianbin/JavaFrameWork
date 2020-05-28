package com.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Classname AdminMapper
 * @Description Admin Mapper
 * @Date 2020/5/27 16:23
 * @Created by 125937
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    List<Admin> testSelect();
}
