package com.sunten.hrms.td.domain;

import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdSafetyTrainingDept extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String deptName;
    private String department;
    private Boolean enabledFlag;
}
