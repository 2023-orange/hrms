package com.sunten.hrms.ac.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author ljw
 * @since 2020-09-17
 */
@Data
public class AcRegimeQueryCriteria implements Serializable {
    private Boolean enabled;
    private String regimeName;
    private Long arId;

}
