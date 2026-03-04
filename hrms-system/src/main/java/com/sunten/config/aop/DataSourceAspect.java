package com.sunten.config.aop;

import com.sunten.config.DataSourceContextHolder;
import com.sunten.config.DataSourceKeyEnum;
import com.sunten.config.annotation.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Slf4j
@Aspect
@Order(-1)
public class DataSourceAspect {

    @Pointcut("@annotation(com.sunten.config.annotation.DataSource) || execution(* com.sunten.*.*.dao.*Dao.*(..))||execution(* com.baomidou.mybatisplus.core.mapper.*Mapper.*(..)))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object doBefore(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        DataSource dataSource = AnnotationUtils.findAnnotation(method, DataSource.class);
        if (dataSource == null) {
            dataSource = AnnotationUtils.findAnnotation(method.getDeclaringClass(), DataSource.class);
        }
        log.info(String.valueOf(dataSource));
        DataSourceKeyEnum keyEnum = DataSourceKeyEnum.getDataSourceKey(dataSource);
        log.info("选择的数据源：" + keyEnum.getValue());
        DataSourceContextHolder.setDataSource(keyEnum.getValue());
        Object o = pjp.proceed();
        DataSourceContextHolder.clear();
        return o;
    }

    @Pointcut("execution(* com.baomidou.mybatisplus.extension.service.IService.*Batch*(..)))")
    public void pointCutBatch() {
    }

    //对mybatisplus批量操作切面
    @Around("pointCutBatch()")
    public Object doBeforeBatch(ProceedingJoinPoint pjp) throws Throwable {
        DataSourceContextHolder.setDataSource(DataSourceKeyEnum.HRMS.getValue());
        Object o = pjp.proceed();
        DataSourceContextHolder.clear();
        return o;
    }
}
