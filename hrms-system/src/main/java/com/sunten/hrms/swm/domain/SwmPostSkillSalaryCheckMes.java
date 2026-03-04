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
public class SwmPostSkillSalaryCheckMes extends BaseEntity {
    private static final long serialVersionUID = 1L;
    // 检查人员类别
    private String checkEmployeeCategory;
    // 检查非包干人员1
    private String checkNotDivisionContractFlag1;
    // 检查非包干人员2
    private String checkNotDivisionContractFlag2;
    // 检查包干人员
    private String checkIsDivisionContractFlag;
}
