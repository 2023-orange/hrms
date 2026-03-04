package com.sunten.hrms.fnd.dto;

import com.sunten.hrms.fnd.domain.AdvancedQuery;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class QueryCriteria {
    /**
     * 部门id,查询该部门下所有子节点信息
     */
    private Long deptAllId;

    /**
     * 部门id，只查询本部门下的一级节点信息
     */
    private Long deptId;

    /**
     * 部门id及其下所有子ID集合,根据部门id获取
     */
    private Set<Long> deptIds;

    private Long employeeId;

    /**
     * 列名
     */
    private String colName;
    /**
     * 符号
     */
    private String symbol;
    /**
     * 输入值
     */
    private String value;

    // 解析后的高级查询内容
    private List<AdvancedQuery> advancedQuerys;

    // 前台传过来时的高级查询内容的JSON串
    private String advancedQuerysStr;

    private Boolean nullFlag;

}
