package com.sunten.hrms.swm.vo;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeDistinctTings {
    // 部门
    List<String> departments;
    // 科室
    List<String> administrativeOffices;
    // 班组
    List<String> teams;
    // 岗位
    List<String> stations;
    // 员工类别
    List<String> employeeCategorys;
    // 职级
    List<String> ranks;
    // 技术职级
    List<String> technicalRanks;
    // 技能级别
    List<String> skillLevels;
    // 职类
    List<String> categorys;
    // 职种
    List<String> jobs;
    // 职位
    List<String> positions;
    // 职称
    List<String> titles;
    // 学历
    List<String> educations;

    List<String> serviceDepartments;
}
