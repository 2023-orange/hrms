package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author liangjw
 * @since 2020-10-15
 */
@Data
public class PmEmployeeJobSnapshotQueryCriteria implements Serializable {

    private LocalDate date;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    private Boolean workshopAttendanceFlag;

    private Boolean workAttendanceFlag;

}
