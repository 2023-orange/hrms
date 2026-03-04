package com.sunten.hrms.td.dto;

import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author liangjw
 * @since 2021-05-19
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TdPlanQueryCriteria extends FndDynamicQueryBaseCriteria {

//    // 解析后的高级查询内容
//    private List<AdvancedQuery> advancedQuerys;
//    // 前台传过来时的高级查询内容的JSON串
//    private String advancedQuerysStr;
//
//    private String colName;
//
//    private String symbol;
//
//    private String value;

    private Boolean enabledFlag;

    private Boolean showFlag;

    private Long deptId;

    private Set<Long> deptIds;

    private Long deptChargeId;

    private Long planChargeId;
}
