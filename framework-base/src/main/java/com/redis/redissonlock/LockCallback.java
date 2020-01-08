package com.redis.redissonlock;

/**
 * @Classname LockCallback
 * @Description TODO
 * @Date 2020/1/7 19:09
 * @Created by 125937
 */
public interface LockCallback<P> {

    P run(Object object);

    String getLockName(Object object);
}
