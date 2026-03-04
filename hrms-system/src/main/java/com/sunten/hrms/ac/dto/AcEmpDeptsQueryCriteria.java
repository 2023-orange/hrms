package com.sunten.hrms.ac.dto;

import com.sunten.hrms.fnd.dto.AdvancedCriteriaQuery;
import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author liangjw
 * @since 2020-12-09
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AcEmpDeptsQueryCriteria extends FndDynamicQueryBaseCriteria {
    private Long employeeId;

    private Boolean enabledFlag;

    private Long roleId;

    private String dataType;

}
