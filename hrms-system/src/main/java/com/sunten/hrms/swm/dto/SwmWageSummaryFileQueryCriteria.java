package com.sunten.hrms.swm.dto;

import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.AdvancedCriteriaQuery;
import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author liangjw
 * @since 2020-12-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SwmWageSummaryFileQueryCriteria extends FndDynamicQueryBaseCriteria {
    private String incomePeriod;

    // 人事员工ID
    private Long employeeId;
}
