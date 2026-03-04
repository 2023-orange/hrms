package com.sunten.hrms.fnd.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author liangjw
 * @since 2020-10-15
 */
@Data
public class FndDeptSnapshotQueryCriteria implements Serializable {
    private LocalDate date;

    private Boolean enabled;

    private String dateStr;

    private Long deptId;

}
