package com.sunten.hrms.ac.dto;

import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author zouyp
 * @since 2023-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AcOvertimeManagementQueryCriteria extends FndDynamicQueryBaseCriteria {
    private LocalDate beginDate;
    private LocalDate endDate;
}
