package com.sunten.hrms.ac.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * @author ljw
 * @since 2020-09-17
 */
@Data
public class AcExceptionDisposeQueryCriteria implements Serializable {
    private String employeeName;
    private Set<Long> deptIds;

    private Long employeeId;
    private LocalDate beginDate;
    private LocalDate endDate;
}
