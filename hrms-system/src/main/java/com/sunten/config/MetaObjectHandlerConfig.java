package com.sunten.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sunten.hrms.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author: localhost
 * @program: mybatis-plus
 * @description: 自动填充公共字段（上传时间）
 * @create: 2019-09-03 20:01
 **/
//@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        setFieldValByName("createTime", now, metaObject);
        setFieldValByName("updateTime", now, metaObject);
        Long userId ;
        try{
            userId= SecurityUtils.getUserId();
        }catch (Exception ex){
            userId= Long.valueOf(-1);
        }
        setFieldValByName("createBy", userId, metaObject);
        setFieldValByName("updateBy", userId, metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        setFieldValByName("updateTime", now, metaObject);
        Long userId ;
        try{
            userId= SecurityUtils.getUserId();
        }catch (Exception ex){
            userId= Long.valueOf(-1);
        }
        setFieldValByName("updateBy", userId, metaObject);
    }
}
