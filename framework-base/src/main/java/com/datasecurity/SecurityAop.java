package com.datasecurity;

import com.annotations.DataSecurityFilter;
import com.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @Classname SecurityAop
 * @Description 加密切面
 * @Date 2020/1/6 17:05
 * @Created by 125937
 */
@Component
@Aspect
@Slf4j
public class SecurityAop {

    @Pointcut("@annotation(com.annotations.DataSecurityFilter)")
    public void dataSecurityPointCut() {
    }

    @Around(value ="dataSecurityPointCut()")
    public Object dataSecurityService(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Object[] args = joinPoint.getArgs();
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        log.info("SecurityAop dataSecurityService method:{}.{}",sign.getDeclaringType().getName(),sign.getMethod().getName());
        Method method=sign.getMethod();
        DataSecurityFilter annotation = method.getAnnotation(DataSecurityFilter.class);
        if(ArrayUtils.isNotEmpty(args)){
            result=joinPoint.proceed(args);
        }else{
            result=joinPoint.proceed();
        }
        if (Objects.nonNull(result) && annotation.includes().length > 0) {
            List<String> colList= Arrays.asList(annotation.includes());
            if (result instanceof Collection) {
                for (Object obj : (Collection) result) {
                    encodeCol(obj, colList);
                }
            } else {
                encodeCol(result,colList);
            }
        }
        return result;
    }

    private void encodeCol(Object obj, List<String> colList) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (colList.contains(field.getName())) {
                field.setAccessible(true);
                Object val = field.get(obj);
                if (Objects.nonNull(val)) {
                    field.set(obj, CommonUtils.encodeByRsa(String.valueOf(val)));
                }
            }
        }
    }
}
