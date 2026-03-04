package com.sunten.hrms.re.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2021-04-23
 */
@Data
public class ReDemandJobQueryCriteria implements Serializable {
    private Boolean enabledFlag;
    private Long demandId;
}
