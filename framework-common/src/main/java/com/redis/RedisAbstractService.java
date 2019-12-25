package com.redis;

import com.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RedisAbstractService {

    @Autowired
    protected RedisTemplate redisTemplate;

    /**
     * 默认过期时长，单位：秒
     */
    protected  static final long DEFAULT_EXPIRE = 60 * 60 * 24;

    /**
     * 不设置过期时长
     */
    protected static final long NOT_EXPIRE = -1;

    public boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 重名名key，如果newKey已经存在，则newKey的原值被覆盖
     *
     * @param oldKey
     * @param newKey
     */
    public void renameKey(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * newKey不存在时才重命名
     *
     * @param oldKey
     * @param newKey
     * @return 修改成功返回true
     */
    public boolean renameKeyNotExist(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 删除key
     *
     * @param key
     */
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除多个key
     *
     * @param keys
     */
    public void deleteKey(String... keys) {
        Set<String> kSet = Stream.of(keys).map(k -> k).collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }

    /**
     * 删除Key的集合
     *
     * @param keys
     */
    public void deleteKey(Collection<String> keys) {
        Set<String> kSet = keys.stream().map(k -> k).collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }

    /**
     * 设置key的生命周期
     *
     * @param key
     * @param time
     * @param timeUnit
     */
    public void expireKey(String key, long time, TimeUnit timeUnit) {
        redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 指定key在指定的日期过期
     *
     * @param key
     * @param date
     */
    public void expireKeyAt(String key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    /**
     * 查询key的生命周期
     *
     * @param key
     * @param timeUnit
     * @return
     */
    public long getKeyExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 将key设置为永久有效
     *
     * @param key
     */
    public void persistKey(String key) {
        redisTemplate.persist(key);
    }

    /**
     * set操作
     * @param key
     * @param value
     */
    public void set(String key,Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * get操作
     * @param key
     */
    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * set操作，过期时间（秒）
     * @param key
     * @param value
     * @param timeOut
     */
    public void set(String key,Object value,long timeOut){
        redisTemplate.opsForValue().set(key, JsonUtils.deSerializable(value),timeOut, TimeUnit.SECONDS);
    }

    /**
     * 原子操作
     * @param key
     * @return
     */
    public long increment(String key){
        return redisTemplate.opsForValue().increment(key,1L);
    }

    /**
     * 原子操作
     * @param key
     * @return
     */
    public long deIncrement(String key){
        return redisTemplate.opsForValue().increment(key,-1L);
    }

    /**
     * 原子操作，指定步长
     * @param key
     * @param inc
     * @return
     */
    public long increment(String key,long inc){
        return redisTemplate.opsForValue().increment(key,inc);
    }

    /**
     * hasSet操作
     * @param key
     * @param hKey
     * @param value
     */
    public void hashSet(String key,String hKey,Object value){
        redisTemplate.opsForHash().put(key,hKey,value);
    }

    /**
     * hasGet操作
     * @param key
     * @param hKey
     */
    public Object hashGet(String key,String hKey){
        Object val= redisTemplate.opsForHash().get(key,hKey);
        return val;
    }

    /**
     * hasSet操作
     * @param key
     * @param hKey
     */
    public void hashDel(String key,String hKey){
        redisTemplate.opsForHash().delete(key,hKey);
    }

    /**
     * List操作
     * @param key
     * @param value
     */
    public void rightPush(String key,Object value){
        redisTemplate.opsForList().rightPush(key,value);
    }

    /**
     * List操作
     * @param key
     * @param value
     */
    public void leftPush(String key,Object value){
        redisTemplate.opsForList().leftPush(key,value);
    }

    public Object leftPop(String key){
       Object value= redisTemplate.opsForList().leftPop(key,1,TimeUnit.SECONDS);
       return value;
    }

    public Object rightPop(String key){
        Object value= redisTemplate.opsForList().rightPop(key,1,TimeUnit.SECONDS);
        return value;
    }
    
    public List<Object> listAll(String key){
        List<Object> value= redisTemplate.opsForList().range(key,1,-1);
        return value;
    }
}
