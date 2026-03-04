package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author batan
 * @since 2020-08-04
 */
@Data
public class PmEmployeeSocialrelationsQueryCriteria implements Serializable {
    private Long employeeId;
    private Boolean enabled;
    private Boolean inFactoryFlag;
    private String relationship;


}
