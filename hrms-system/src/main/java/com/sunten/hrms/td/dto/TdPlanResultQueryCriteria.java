package com.sunten.hrms.td.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2021-06-16
 */
@Data
public class TdPlanResultQueryCriteria implements Serializable {
    private Long planId;

    private Boolean enabledFlag;

    private String checkMethod;

}
