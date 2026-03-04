package com.sunten.hrms.td.vo;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString(callSuper = true)
public class PmPlanExcelVo {

    private Long id;

    private Long planId;

    private Long planImplementId;

    private String trainingName;

    private String trainingMethod;

    private String trainingContent;

    private String checkMethod;

    private String teacher;

    private String trainingAddress;

    private Double trainingTimeQuantity;

    private Long employeeQuantity;

    private String deptName;

    private String department;

    private String team;

    private String name;

    private Double duration;

    private Double evaluate;

    private Double grade;

    private Double scoreLine;

    private LocalDate beginDate;

    private LocalDate endDate;

    private Long passNumber;

}
