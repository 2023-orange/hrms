package com.sunten.hrms.swm.dto;

import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.AdvancedCriteriaQuery;
import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SwmFloatingWageQueryCriteria extends FndDynamicQueryBaseCriteria {
    private Boolean frozenFlag;

    private String period;

    private Long id;

    private Boolean grantFlag;

    private String administrativeOffice;

    private String department;

    private Boolean generationDifferentiationFlag;

    private String employeeCategory;

    private String sorts;


    private List<SwmEmpDeptDTO> swmEmpDepts;



}
