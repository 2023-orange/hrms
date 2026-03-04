package com.sunten.hrms.td.vo;

import lombok.Data;

import java.util.Set;

@Data
public class SafetyTrainingDeptVo {
    private Long id;
    private String deptName;
    private String deptCode;
    private Set<SafetyTrainingDeptVo> safetyTrainingDeptVoSet;
}
