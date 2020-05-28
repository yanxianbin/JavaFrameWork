package com.service;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;


import java.util.List;

/**
 * @Classname BaseService
 * @Description Service 通用类
 * @Date 2020/5/27 16:40
 * @Created by 125937
 */
public abstract class GenericService<T> {

    protected BaseMapper<T> baseMapper;

    public GenericService(BaseMapper<T> mapper){
        this.baseMapper=mapper;
    }

    abstract BaseMapper<T> getMapper();

    /**
     * 插入数据
     * @param entity
     * @return
     */
    public int insert(T entity){
        int row=baseMapper.insert(entity);
        return row;
    }

    public int insertAllColumn(T entity){
        int row=baseMapper.insertAllColumn(entity);
        return row;
    }

    public List<T> selectList(T entity){
        EntityWrapper<T> entityWrapper=new EntityWrapper<>(entity);
        List<T> list= baseMapper.selectList(entityWrapper);
        return list;
    }
}
