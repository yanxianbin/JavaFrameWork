package com.mongodb;

import com.entity.BaseEntity;
import com.mongodb.mode.PageInfo;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.Pair;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Classname MongodbBaseDao
 * @Description mongoDB 操作数据库通用类
 * @Date 2020/1/2 17:15
 * @Created by 125937
 */
@Slf4j
public class MongodbBaseDao<T extends BaseEntity> {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(T t) {
        mongoTemplate.insert(t);
    }
    public DeleteResult delete(T t){
        return mongoTemplate.remove(t);
    }
    public DeleteResult deleteById(Object id){
        T t = mongoTemplate.findById(id, getTClass());
        return delete(t);
    }
    public DeleteResult delete(Query query){
        return mongoTemplate.remove(query,getTClass());
    }

    /**
     * 根据实体查询数据
     * @param entity
     * @return
     */
    public List<T> selectAll(T entity) throws IllegalAccessException {
        Query query = getQuery(entity);
        List<T> returnList = find(query);
        return returnList;
    }

    private Query getQuery(T entity) throws IllegalAccessException {
        Field[] fields = entity.getClass().getSuperclass().getDeclaredFields();
        List<Field> allFields = new ArrayList<>(Arrays.asList(fields));
        allFields.addAll(Arrays.asList(entity.getClass().getDeclaredFields()));
        Criteria criteria = new Criteria();
        if (!CollectionUtils.isEmpty(allFields)) {
            for (Field field : allFields) {
                field.setAccessible(true);
                String key = field.getName();
                if (field.getName().equals("id")) {
                    key = "_id";
                }
                Object val = field.get(entity);
                if(Objects.nonNull(val)) {
                    criteria = criteria.and(key).is(val);
                }
            }
        }
        return new Query(criteria);
    }

    /**
     * 分页查询
     * @param pageInfo
     * @return
     * @throws IllegalAccessException
     */
    public PageInfo<T> searchPage(PageInfo<T> pageInfo) throws IllegalAccessException {
        Query query= getQuery(pageInfo.getQueryCondition());
        int rowTotal=Integer.valueOf(String.valueOf(count(query))).intValue();
        pageInfo.setRowTotal(rowTotal);
        final Integer pages = (int) Math.ceil(rowTotal / (double) pageInfo.getPageSize());
        query=query.with(Sort.by(pageInfo.getOrderCondition()));
        if (pageInfo.getPageIndex() <= 0 || pageInfo.getPageIndex() > pages) {
            pageInfo.setPageIndex(1);
        }
        int skip = pageInfo.getPageSize() * (pageInfo.getPageIndex() - 1);
        query=query.skip(skip).limit(pageInfo.getPageSize());
        List<T> list= find(query);
        pageInfo.setRows(list);
        pageInfo.setPageTotal(pages);
        return pageInfo;
    }

    public void update(T t) {
        mongoTemplate.save(t);
    }
    public T findById(Object id) {
        return mongoTemplate.findById(id,getTClass());
    }
    public List<T> findAll() {
        return mongoTemplate.findAll(getTClass());
    }
    public List<T> find(Query query){
        return mongoTemplate.find(query,getTClass());
    }

    public boolean exists(Query query){
        return  mongoTemplate.exists(query,getTClass());
    }
    public boolean exists(T t){
        return  exists(getIdQueryFor(t));
    }
    public boolean exitsById(Object id){
        return exists(findById(id));
    }
    public long count(Query query) {
        return mongoTemplate.count(query,getTClass());
    }
    private Class<T> getTClass() {
        Class<T> tClass =(Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }
    private Pair<String, Object> extractIdPropertyAndValue(Object object) {
        Assert.notNull(object, "Id cannot be extracted from 'null'.");
        Class<?> objectType = object.getClass();
        if (object instanceof Document) {
            return Pair.of("_id", ((Document)object).get("_id"));
        } else {
            MongoPersistentEntity<?> entity = (MongoPersistentEntity)mongoTemplate.getConverter().getMappingContext().getPersistentEntity(objectType);
            if (entity != null && entity.hasIdProperty()) {
                MongoPersistentProperty idProperty = (MongoPersistentProperty)entity.getIdProperty();
                return Pair.of(idProperty.getFieldName(), entity.getPropertyAccessor(object).getProperty(idProperty));
            } else {
                throw new MappingException("No id property found for object of type " + objectType);
            }
        }
    }

    private Query getIdQueryFor(Object object) {
        Pair<String, Object> id = this.extractIdPropertyAndValue(object);
        return new Query(Criteria.where((String)id.getFirst()).is(id.getSecond()));
    }
}
