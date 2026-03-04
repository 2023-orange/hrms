package com.sunten.hrms.exception;

import org.springframework.util.StringUtils;

/**
 * @author batan
 * @since 2018-11-23
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class clazz, String field, String val) {
        super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), field, val));
    }

    private static String generateMessage(String entity, String field, String val) {
        return "表 " + StringUtils.capitalize(entity)
                + " 的字段" + field + " 值 " + val + " 不存在";
    }
}
