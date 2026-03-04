package com.sunten.hrms.ac.dto;

import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.AdvancedCriteriaQuery;
import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author liangjw
 * @since 2020-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AcClockRecordQueryCriteria extends FndDynamicQueryBaseCriteria {
    private Long deptId;
    private String employeeName;
    private List<PmEmployeeDTO> employees;
    private Set<Long> deptIds;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Boolean adminFlag;
    private Long employeeId;

    private Boolean fakeFlag;
}
