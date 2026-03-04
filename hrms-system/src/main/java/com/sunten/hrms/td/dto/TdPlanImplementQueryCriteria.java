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
 * @since 2021-05-25
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TdPlanImplementQueryCriteria extends FndDynamicQueryBaseCriteria {

    //备注： 动态查询要求删除colName  symbol  value  advancedQuerys   advancedQuerysStr
    //但其它页面有这个业务需求，不能删除colName  symbol  value  advancedQuerys   advancedQuerysStr


// 解析后的高级查询内容

    private List<AdvancedQuery> advancedQuerys;
// 前台传过来时的高级查询内容的JSON串

    private String advancedQuerysStr;

    private String colName;

    private String symbol;

    private String value;

    private Boolean enabledFlag;

    private String approvalStatus;

    private Long deptId;

    private Set<Long> deptIds;

    private Long planChargeId;

    private Long deptChargeId;

    private Long requestBy;


}
