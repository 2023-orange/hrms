package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author batan
 * @since 2020-08-04
 */
@Data
public class PmEmployeeTitleTempQueryCriteria implements Serializable {
    private Long titleId;
    private Boolean enabled;
    private Long employeeId;
    private String checkFlag;
    private String instructionsFlag;
}
