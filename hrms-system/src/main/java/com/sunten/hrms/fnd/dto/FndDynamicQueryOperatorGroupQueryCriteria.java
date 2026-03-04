package com.sunten.hrms.fnd.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author batan
 * @since 2022-07-26
 */
@Data
public class FndDynamicQueryOperatorGroupQueryCriteria implements Serializable {
    private Boolean enabled;
    private Boolean expandDetail;
}
