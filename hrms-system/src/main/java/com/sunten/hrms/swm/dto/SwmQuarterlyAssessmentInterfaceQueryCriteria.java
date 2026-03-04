package com.sunten.hrms.swm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2022-05-13
 */
@Data
public class SwmQuarterlyAssessmentInterfaceQueryCriteria implements Serializable {
    private Long groupId;
    private String dataStatus;
}
