package com.redis;

import com.utils.SpringUtils;
import org.springframework.data.redis.core.*;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RedisAbstractService {

    private static RedisTemplate redisTemplate;

    protected static RedisTemplate getRedisTemplate(){
        if(redisTemplate==null){
            redisTemplate=SpringUtils.getBean("redisTemplate");
        }
        return redisTemplate;
    }

    /**
     * 默认过期时长，单位：秒
     */
    public static final long DEFAULT_EXPIRE = 60 * 60 * 24;

    /**
     * 不设置过期时长
     */
    protected static final long NOT_EXPIRE = -1;

    public static boolean existsKey(String key) {
        return getRedisTemplate().hasKey(key);
    }

    /**
     * 重名名key，如果newKey已经存在，则newKey的原值被覆盖
     *
     * @param oldKey
     * @param newKey
     */
    public static void renameKey(String oldKey, String newKey) {
        getRedisTemplate().rename(oldKey, newKey);
    }

    /**
     * newKey不存在时才重命名
     *
     * @param oldKey
     * @param newKey
     * @return 修改成功返回true
     */
    public static boolean renameKeyNotExist(String oldKey, String newKey) {
        return getRedisTemplate().renameIfAbsent(oldKey, newKey);
    }

    /**
     * 删除key
     *
     * @param key
     */
    public static void deleteKey(String key) {
        getRedisTemplate().delete(key);
    }

    /**
     * 删除多个key
     *
     * @param keys
     */
    public static void deleteKey(String... keys) {
        Set<String> kSet = Stream.of(keys).map(k -> k).collect(Collectors.toSet());
        getRedisTemplate().delete(kSet);
    }

    /**
     * 删除Key的集合
     *
     * @param keys
     */
    public static void deleteKey(Collection<String> keys) {
        Set<String> kSet = keys.stream().map(k -> k).collect(Collectors.toSet());
        getRedisTemplate().delete(kSet);
    }

    /**
     * 设置key的生命周期
     *
     * @param key
     * @param time
     * @param timeUnit
     */
    public static void expireKey(String key, long time, TimeUnit timeUnit) {
        getRedisTemplate().expire(key, time, timeUnit);
    }

    /**
     * 设置key的生命周期 默认一天
     *
     * @param key
     */
    public static void expireKey(String key) {
        getRedisTemplate().expire(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 指定key在指定的日期过期
     *
     * @param key
     * @param date
     */
    public void expireKeyAt(String key, Date date) {
        getRedisTemplate().expireAt(key, date);
    }

    /**
     * 查询key的生命周期
     *
     * @param key
     * @param timeUnit
     * @return
     */
    public static long getKeyExpire(String key, TimeUnit timeUnit) {
        return getRedisTemplate().getExpire(key, timeUnit);
    }

    /**
     * 将key设置为永久有效
     *
     * @param key
     */
    public static void persistKey(String key) {
        getRedisTemplate().persist(key);
    }

    /**
     * set操作
     * @param key
     * @param value
     */
    public static void set(String key,Object value){
        getRedisTemplate().opsForValue().set(key, value);
    }

    /**
     * get操作
     * @param key
     */
    public static Object get(String key){
        return getRedisTemplate().opsForValue().get(key);
    }

    /**
     * set操作，过期时间（秒）
     * @param key
     * @param value
     * @param timeOut
     */
    public static void set(String key,Object value,long timeOut){
        getRedisTemplate().opsForValue().set(key, value,timeOut, TimeUnit.SECONDS);
    }

    /**
     * 原子操作
     * @param key
     * @return
     */
    public static long increment(String key){
        return getRedisTemplate().opsForValue().increment(key,1L);
    }

    /**
     * 原子操作
     * @param key
     * @return
     */
    public static long deIncrement(String key){
        return getRedisTemplate().opsForValue().increment(key,-1L);
    }

    /**
     * 原子操作，指定步长
     * @param key
     * @param inc
     * @return
     */
    public static long increment(String key,long inc){
        return getRedisTemplate().opsForValue().increment(key,inc);
    }

    /**
     * hasSet操作
     * @param key
     * @param hKey
     * @param value
     */
    public static void hashSet(String key,String hKey,Object value){
        getRedisTemplate().opsForHash().put(key,hKey,value);
    }

    /**
     * hasGet操作
     * @param key
     * @param hKey
     */
    public static Object hashGet(String key,String hKey){
        Object val= getRedisTemplate().opsForHash().get(key,hKey);
        return val;
    }

    /**
     * hasSet操作
     * @param key
     * @param hKey
     */
    public static void hashDel(String key,String hKey){
        getRedisTemplate().opsForHash().delete(key,hKey);
    }

    /**
     * List操作
     * @param key
     * @param value
     */
    public static void rightPush(String key,Object value){
        getRedisTemplate().opsForList().rightPush(key,value);
    }

    /**
     * List操作
     * @param key
     * @param value
     */
    public static void leftPush(String key,Object value){
        getRedisTemplate().opsForList().leftPush(key,value);
    }

    public static Object leftPop(String key){
       Object value= getRedisTemplate().opsForList().leftPop(key,1,TimeUnit.SECONDS);
       return value;
    }

    public static Object rightPop(String key){
        Object value= getRedisTemplate().opsForList().rightPop(key,1,TimeUnit.SECONDS);
        return value;
    }
    
    public static List<Object> listAll(String key){
        List<Object> value= getRedisTemplate().opsForList().range(key,1,-1);
        return value;
    }
}
