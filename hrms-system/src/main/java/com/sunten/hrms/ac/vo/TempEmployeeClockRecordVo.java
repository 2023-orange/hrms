package com.sunten.hrms.ac.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class TempEmployeeClockRecordVo {
    private Long id;
    private String deptName;
    private String departmentName;
    private String teamName;
    private String name;
    private String workCard;
    private LocalDate nowDate;
    private LocalTime minClockTime;
    private LocalTime maxClockTime;
    private Boolean leaveFlag;
    private Boolean workFlag;

    private Boolean dayOffFlag;
}
