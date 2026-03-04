package com.sunten.hrms.re.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2021-04-23
 */
@Data
public class ReDemandJobDescribesQueryCriteria implements Serializable {
    private Long demandJobId;
    private Boolean enabledFlag;
}
