package com.sunten.hrms.ac.dto;

import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

/**
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AcClockRecordRestQueryCriteria extends FndDynamicQueryBaseCriteria {
    /**
     * 部门ID
     */
    private Long deptId;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Set<Long> deptIds;
    private Boolean adminFlag;
    private Long employeeId;
    private String sorts;
}
