package com.sunten.hrms.td.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2022-03-08
 */
@Data
public class TdPlanCheckMethodQueryCriteria implements Serializable {

    private Long planImplementId;

    private Boolean enabledFlag;

}
