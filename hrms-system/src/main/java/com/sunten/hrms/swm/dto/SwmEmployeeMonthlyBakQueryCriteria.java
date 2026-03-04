package com.sunten.hrms.swm.dto;

import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.AdvancedCriteriaQuery;
import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import com.sunten.hrms.fnd.dto.QueryCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author liangjw
 * @since 2021-09-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SwmEmployeeMonthlyBakQueryCriteria extends FndDynamicQueryBaseCriteria {

    private String bakDate;

    private LocalDate bakDateLocal;

    private String deptCode;

    private String department;

    private String administrativeOffice;

    private Boolean managerFlag;

    private String incomePeriod;

}
