package com.sunten.hrms.fnd.domain;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class DynamicQuery {
    private Integer levels;
    private String condition;
    private Boolean notCondition;
    private String type;
    private String queryTable;
    private String field;
    private String operator;
    private String value;
    private String jdbcType;
    private String specialSql;
    private String fieldType;
    private List<DynamicQuery> rules;
}
