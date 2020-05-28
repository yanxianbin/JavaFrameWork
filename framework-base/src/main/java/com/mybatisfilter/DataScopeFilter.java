package com.mybatisfilter;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

/**
 * @Classname SqlFilter
 * @Description sql拦截器
 * @Date 2020/5/27 15:58
 * @Created by 125937
 */
@Component
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})
@Slf4j
public class DataScopeFilter implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        log.info("SqlFilter, methodName：{}, commandType: {} sql:{}", invocation.getMethod().getName(), ms.getSqlCommandType(), boundSql.getSql());
        if (boundSql.getSql().contains("<datascope>")) {
            //todo 调用对应的服务，获取到字段名称和对应的值 用 in 方式串起来
            String sql = boundSql.getSql();
            String scopeSql = " account=2";
            if (!sql.contains(" where ")) {
                scopeSql = " where " + scopeSql;
            }
            sql = sql.replace("<datascope>", scopeSql);
            boundSql = new BoundSql(ms.getConfiguration(), sql, ms.getParameterMap().getParameterMappings(), parameterObject);
            //可以对参数做各种处理
            CacheKey cacheKey = executor.createCacheKey(ms, parameterObject, rowBounds, boundSql);
            return executor.query(ms, parameterObject, rowBounds, resultHandler, cacheKey, boundSql);
        } else {
            return invocation.proceed();
        }
    }
}
