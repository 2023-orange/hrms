package com.sunten.hrms.td.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2022-03-10
 */
@Data
public class TdTrainingEvaluationScoreQueryCriteria implements Serializable {
    private Boolean enabledFlag;

    private Long planImplementId;
}
