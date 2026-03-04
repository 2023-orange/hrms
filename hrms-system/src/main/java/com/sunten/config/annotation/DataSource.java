package com.sunten.config.annotation;

import com.sunten.config.DataSourceKeyEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    DataSourceKeyEnum value();
}
