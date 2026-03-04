package com.sunten.hrms.td.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2021-06-21
 */
@Data
public class TdPlanImplementDeptQueryCriteria implements Serializable {

    private Long planImplementId;

    private Boolean enabledFlag;

    private Long deptId;

}
