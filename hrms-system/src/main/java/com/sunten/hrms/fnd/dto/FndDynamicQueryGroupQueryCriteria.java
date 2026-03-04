package com.sunten.hrms.fnd.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author batan
 * @since 2022-07-26
 */
@Data
public class FndDynamicQueryGroupQueryCriteria implements Serializable {
    private Boolean enabled;
    private String blurry;
}
