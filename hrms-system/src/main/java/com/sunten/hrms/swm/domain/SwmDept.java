package com.sunten.hrms.swm.domain;

import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmDept extends BaseEntity {
    private static final long serialVersionUID = 1L;
    // 部门
    private String department;
    // 科室
    private String administrativeOffice;

}
