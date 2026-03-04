package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author batan
 * @since 2020-08-04
 */
@Data
public class PmEmployeePostotherQueryCriteria implements Serializable {
    private Long employeeId;
    private Boolean enabled;

}
