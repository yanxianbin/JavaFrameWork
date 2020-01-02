package com.mongodb;

import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @Classname MongodbBaseDao
 * @Description TODO
 * @Date 2020/1/2 17:15
 * @Created by 125937
 */
public class MongodbBaseDao<T> {
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
