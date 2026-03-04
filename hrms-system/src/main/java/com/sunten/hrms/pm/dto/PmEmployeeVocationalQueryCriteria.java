package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author batan
 * @since 2020-08-04
 */
@Data
public class PmEmployeeVocationalQueryCriteria implements Serializable {
    private Long employeeId;
    private Boolean newVocationalFlag;
    private Boolean enabled;

}
