package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author batan
 * @since 2020-08-04
 */
@Data
public class PmEmployeeVocationalTempQueryCriteria implements Serializable {
    private Long vocationalId;
    private Boolean enabled;

    private Long employeeId;
    private String instructionsFlag;
    private String checkFlag;
}
