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
 * @since 2021-06-30
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TdCredentialQueryCriteria extends FndDynamicQueryBaseCriteria {
    private Long deptId;

    private Set<Long> deptIds;

    private Boolean enabledFlag;

//    private String value;
//
//    private String colName;
//
//    private String symbol;
//
//    // 解析后的高级查询内容
//    private List<AdvancedQuery> advancedQuerys;
//    // 前台传过来时的高级查询内容的JSON串
//    private String advancedQuerysStr;

    private Long employeeId;

}
