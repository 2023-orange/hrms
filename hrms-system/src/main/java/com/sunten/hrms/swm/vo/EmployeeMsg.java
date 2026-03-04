package com.sunten.hrms.swm.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeMsg {
    // 人事员工id
    private Long empId;

    // 人事员工名称
    private String name;

    // 人事员工工号
    private String workCard;

    // 学历
    private String education;

    // 职称
    private String title;

    // 入职时间
    private LocalDate entryTime;

    // 部门
    private String deptName;

    // 科室
    private String department;

    // 班组
    private String team;

    // 员工岗位
    private String jobName;

}
