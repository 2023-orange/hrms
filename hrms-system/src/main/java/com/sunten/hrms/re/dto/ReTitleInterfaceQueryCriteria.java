package com.sunten.hrms.re.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author batan
 * @since 2020-08-05
 */
@Data
public class ReTitleInterfaceQueryCriteria implements Serializable {
    private Long recruitmentId;
    private Boolean enabled;

}
