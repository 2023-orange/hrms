package com.sunten.hrms.pm.domain;

import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @atuthor xukai
 * @date 2020/9/8 16:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeCheck extends BaseEntity {
    private static final long serialVersionUID = 1L;
    //
//    private Long employeeId;
    private PmEmployee employee;
    private String changeType;

}
