package com.sunten.hrms.fnd.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author batan
 * @since 2022-08-12
 */
@Data
public class FndSuperQueryGroupQueryCriteria implements Serializable {
    private String groupName;
    private Boolean enabledFlag;

}
