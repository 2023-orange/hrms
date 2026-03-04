package com.sunten.hrms.ac.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author ljw
 * @since 2020-09-17
 */
@Data
public class AcRegimeRelationQueryCriteria implements Serializable {
    private Long regimeTimeId;
    private Boolean enabled;
    private Long regimeId;
}
