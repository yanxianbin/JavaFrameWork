package com.mongodb;

import com.Entity.BaseEntity;
import com.mongodb.client.result.DeleteResult;
import com.utils.IdGeneratorClient;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * @Classname MongoGenericService
 * @Description TODO
 * @Date 2020/1/2 17:55
 * @Created by 125937
 */
@Component
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
