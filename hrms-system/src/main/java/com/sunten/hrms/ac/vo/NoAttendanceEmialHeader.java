package com.sunten.hrms.ac.vo;

import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;

@Data
public class NoAttendanceEmialHeader {
    private PmEmployee employee;

    private String jobName;

    private String email;

    private Long deptId;
}
