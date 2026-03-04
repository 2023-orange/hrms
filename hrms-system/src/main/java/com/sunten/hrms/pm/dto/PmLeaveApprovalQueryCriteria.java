package com.sunten.hrms.pm.dto;

import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * @author liangjw
 * @since 2021-05-07
 */
@Data
@ToString(callSuper = true)
public class PmLeaveApprovalQueryCriteria extends FndDynamicQueryBaseCriteria {
//    // 解析后的高级查询内容
//    private List<AdvancedQuery> advancedQuerys;
//    // 前台传过来时的高级查询内容的JSON串
//    private String advancedQuerysStr;

    private Long deptId;

    private Set<Long> deptIds;

    private Boolean enabledFlag;

    private Long employeeId;

//    private String colName;
//
//    private String symbol;
//
//    private String value;

    private Long createBy;
}
