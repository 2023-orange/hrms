package com.sunten.hrms.td.domain;

import com.sunten.hrms.base.BaseEntity;
import lombok.Data;

@Data
public class TdSafetyTrainingDeptRelation {
    private Long id;
    private Long planId;
    private Long safetyTrainingDeptId;
    private Boolean enabledFlag;
    private String deptName;
    private String department;
}
