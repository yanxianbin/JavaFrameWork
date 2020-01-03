package com.mongodb;

import com.Entity.BaseEntity;
import com.mongodb.mode.PageInfo;
import com.constants.ApplicationException;
import com.constants.ResponseCode;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.lang.Nullable;
import com.utils.IdGeneratorClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Classname MongoGenericService
 * @Description TODO
 * @Date 2020/1/2 17:55
 * @Created by 125937
 */
@Slf4j
public class MongoGenericService<T extends BaseEntity> {

    protected MongodbBaseDao<T> genDao;

    public MongoGenericService(MongodbBaseDao<T> genDaoMap){
        this.genDao=genDaoMap;
    }

    public void insert(T t) {
        if (Objects.isNull(t.getId())) {
            t.setId(IdGeneratorClient.getId());
        }
        if (Objects.isNull(t.getCreateBy())) {
            t.setCreateBy("System");
        }
        if (Objects.isNull(t.getCreateTime())) {
            t.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        if (Objects.isNull(t.getUpdateBy())) {
            t.setUpdateBy("System");
        }
        if (Objects.isNull(t.getUpdateTime())) {
            t.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
        genDao.insert(t);
    }

    public List<T> selectAll(T t){
        try {
            return genDao.selectAll(t);
        }catch (Exception ex){
            log.error("MongoGenericService selectAll error",ex);
            throw new ApplicationException(ResponseCode.SELECT_DATA_ERROR);
        }
    }

    /**
     * 分页查询，默认按照_id排序
     * @param entity
     * @param pageIndex
     * @param pageSize
     * @param orders
     * @return
     */
    public PageInfo<T> searchPage(T entity,int pageIndex,int pageSize,@Nullable String... orders) {
        try {
            PageInfo<T> pageInfo=new PageInfo<>();
            pageInfo.setQueryCondition(entity);
            pageInfo.setPageIndex(pageIndex);
            pageInfo.setPageSize(pageSize);
            List<Sort.Order> orderList=new ArrayList<>();
            if(orders!=null && orders.length>0){
                for(String order : orders){
                    Sort.Order ord=new Sort.Order(Sort.Direction.ASC,order);
                    orderList.add(ord);
                }
            }else{
                Sort.Order ord=new Sort.Order(Sort.Direction.ASC,"_id");
                orderList.add(ord);
            }
            pageInfo.setOrderCondition(orderList);
            return genDao.searchPage(pageInfo);
        } catch (Exception ex) {
            log.error("MongoGenericService searchPage error", ex);
            throw new ApplicationException(ResponseCode.SELECT_DATA_ERROR);
        }
    }

    public DeleteResult delete(T t) {
        return genDao.delete(t);
    }

    public DeleteResult deleteById(Object id) {
        return genDao.deleteById(id);
    }

    public DeleteResult delete(Query query) {
        return genDao.delete(query);
    }

    public void update(T t) {
        if(Objects.isNull(t.getUpdateTime())){
            t.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
        if(Objects.isNull(t.getUpdateBy())){
            t.setUpdateBy("System");
        }
        genDao.update(t);
    }

    public T findById(Object id) {
        return genDao.findById(id);
    }

    public List<T> findAll() {
        return genDao.findAll();
    }

    public List<T> find(Query query) {
        return genDao.find(query);
    }

    public boolean exists(Query query) {
        return genDao.exists(query);
    }

    public boolean exists(T t) {
        return genDao.exists(t);
    }

    public boolean exitsById(Object id) {
        return genDao.exitsById(id);
    }

    public long count(Query query) {
        return genDao.count(query);
    }
}
