package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author xukai
 * @since 2020-08-25
 */
@Data
public class PmEmployeeFamilyTempQueryCriteria implements Serializable {
    private Long employeeId;
    private Boolean enabled;
    private String checkFlag;
    private String instructionsFlag;
    private Long familyId;
}
