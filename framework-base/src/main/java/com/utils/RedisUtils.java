package com.utils;

import com.redis.RedisAbstractService;
import com.redis.redissonlock.LockCallback;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

import java.security.Key;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Classname RedisUtils
 * @Description Redis帮助类
 * @Date 2019/12/27 14:43
 * @Created by 125937
 */
@Slf4j
public class RedisUtils extends RedisAbstractService {
    private static final long MAX_WAIT_TIME=200;

    /**
     * 分布式锁，超时时间，单位：毫秒
     * @param key 锁
     * @param timeOut 过期时间：秒
     * @return
     */
    @SneakyThrows
    public static boolean tryLock(String key, long timeOut) {
        RLock lock = getRedissonClient().getFairLock(key);
        timeOut = timeOut * 1000;
        Boolean isLock = lock.tryLock(timeOut, MAX_WAIT_TIME, TimeUnit.MILLISECONDS);
        return isLock;
    }

    /**
     * 解锁
     * @param key
     */
    public static void releaseLock(String key) {
        RLock lock = getRedissonClient().getFairLock(key);
        lock.unlock();
    }

    /**
     * 加锁并执行方法
     *
     * @param callback
     * @param redisKey
     * @param timeOut
     * @return
     */
    public static Object tryLock(Supplier callback, String redisKey, long timeOut) {
        boolean isLock = tryLock(redisKey, timeOut);
        try {
            if (isLock) {
               return callback.get();
            }else{
                log.error("lock failed:{}", redisKey);
            }
        } catch (Exception ex) {
            log.error("RedisUtils tryLock error:{}", CommonUtils.getExceptionMsg(ex), ex);
        } finally {
            if (isLock) {
                releaseLock(redisKey);
            }
        }
        return null;
    }
}
