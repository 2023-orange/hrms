package com.sunten.hrms.td.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2021-06-17
 */
@Data
public class TdPlanResultInterfaceQueryCriteria implements Serializable {
    private Long planId;

    private Boolean enabledFlag;

    private Long groupId;

    private String checkMethod;
}
