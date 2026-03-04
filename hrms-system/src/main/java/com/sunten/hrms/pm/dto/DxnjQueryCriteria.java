package com.sunten.hrms.pm.dto;

import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import com.sunten.hrms.fnd.dto.QueryCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author liangjw
 * @since 2021-10-08
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
//public class DxnjQueryCriteria extends QueryCriteria implements Serializable {
public class DxnjQueryCriteria extends FndDynamicQueryBaseCriteria{
    private Integer year;
    private String workCard;
//    /**
//     * 列名
//     */
//    private String colName;
//    /**
//     * 符号
//     */
//    private String symbol;
//    /**
//     * 输入值
//     */
//    private String value;
//
//    // 解析后的高级查询内容
//    private List<AdvancedQuery> advancedQuerys;
//
//    // 前台传过来时的高级查询内容的JSON串
//    private String advancedQuerysStr;

    private Long deptId;

    private Set<Long> deptIds;

    /**
     * 部门id,查询该部门下所有子节点信息
     */
    private Long deptAllId;
}
