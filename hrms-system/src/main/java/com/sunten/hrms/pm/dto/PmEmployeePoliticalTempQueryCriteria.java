package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author xukai
 * @since 2021-11-24
 */
@Data
public class PmEmployeePoliticalTempQueryCriteria implements Serializable {
    private Long headerId;
    private Boolean enabled;

    private Long employeeId;
    private String instructionsFlag;
    private String checkFlag;
}
