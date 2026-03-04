package com.sunten.hrms.re.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2022-01-18
 */
@Data
public class ReDemandTrackingQueryCriteria implements Serializable {
    private Boolean enabledFlag;
    private Long demandId;

}
