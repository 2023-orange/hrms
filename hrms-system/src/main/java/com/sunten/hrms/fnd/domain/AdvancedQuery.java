package com.sunten.hrms.fnd.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class AdvancedQuery {
    private String colName; // 字典里条目的名称
    private String colValue; // 高级查询填写的值
    private String symbol; // 高级查询的符号
    private String value; // 高级查询的条目的值

}
