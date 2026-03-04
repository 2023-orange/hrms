package com.sunten.hrms.td.vo;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString(callSuper = true)
public class PlanChildResultVo {
    private Long id;

    private String trainingName;

    private LocalDate beginDate;

    private LocalDate endDate;

    private String trainingContent;

    private String remarks;

    private Long employeeId;
}
