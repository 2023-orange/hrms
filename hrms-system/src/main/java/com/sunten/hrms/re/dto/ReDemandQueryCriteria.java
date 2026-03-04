package com.sunten.hrms.re.dto;

import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author liangjw
 * @since 2021-04-22
 */
@Data
@ToString(callSuper = true)
public class ReDemandQueryCriteria extends FndDynamicQueryBaseCriteria {
    private Boolean enabledFlag;
    private Long deptId;
    private Set<Long> deptIds;
    private Long id;
    private String oaOrder;
    private Long demandBy;

//    private String value;
//    private String colName;
//    private String symbol;
//
//    // 解析后的高级查询内容
//    private List<AdvancedQuery> advancedQuerys;
//    // 前台传过来时的高级查询内容的JSON串
//    private String advancedQuerysStr;

}
