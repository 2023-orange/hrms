package com.sunten.hrms.ac.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author liangjw
 * @since 2020-10-15
 */
@Data
public class AcAttendanceRecordTempQueryCriteria implements Serializable {

    private Long parentId;

    private Long employeeId;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean restFlag;

    private Boolean noSchedulingFlag;

}
