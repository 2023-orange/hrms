package com.sunten.hrms.td.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2022-03-11
 */
@Data
public class TdPlanInspectionSituationQueryCriteria implements Serializable {
    private Boolean enabledFlag;

    private Long planImplementId;

    private String checkMethod;

}
