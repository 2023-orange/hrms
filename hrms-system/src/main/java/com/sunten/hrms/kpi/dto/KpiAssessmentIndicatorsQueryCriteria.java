package com.sunten.hrms.kpi.dto;

import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * @author zhoujy
 * @since 2023-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class KpiAssessmentIndicatorsQueryCriteria extends FndDynamicQueryBaseCriteria {

    private String year;

    private Long currentEmployeeId;

    private Long employeeId;

    private Set<Long> deptIds;
}
