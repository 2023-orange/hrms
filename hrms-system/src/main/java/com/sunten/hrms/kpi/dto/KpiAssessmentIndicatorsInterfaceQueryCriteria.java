package com.sunten.hrms.kpi.dto;

import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsInterface;
import com.sunten.hrms.kpi.domain.KpiInterfaceQueryCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhoujy
 * @since 2023-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KpiAssessmentIndicatorsInterfaceQueryCriteria extends KpiInterfaceQueryCriteria implements Serializable {
    private List<KpiAssessmentIndicatorsInterface> kpiAssessmentIndicatorsInterface;
}
