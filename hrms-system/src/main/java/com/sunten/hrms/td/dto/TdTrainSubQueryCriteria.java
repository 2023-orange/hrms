package com.sunten.hrms.td.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author batan
 * @since 2020-08-04
 */
@Data
public class TdTrainSubQueryCriteria implements Serializable {
    private Long trainId;
    private Boolean enabled;
    private Long employeeId;

}
