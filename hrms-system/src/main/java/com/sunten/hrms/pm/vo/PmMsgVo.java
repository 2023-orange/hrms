package com.sunten.hrms.pm.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class PmMsgVo {
    private Long employeeId;
    private LocalDate entryTime;
    private LocalDate contractStartTime;
    private LocalDate contractEndTime;
    private String deptName;
    private String department;
    private String team;
    private String jobName;
    private String name;
    private String workCard;
    private String idNumber;
    private String tele;
    private Long deptId;
}
