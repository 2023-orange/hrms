package com.sunten.hrms.td.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2021-05-25
 */
@Data
public class TdPlanEmployeeQueryCriteria implements Serializable {
    private Long planImplementId;

    private Long planId;

    private String type;

    private Boolean enabledFlag;
}
