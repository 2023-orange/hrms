package com.sunten.hrms.ac.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author ljw
 * @since 2020-09-17
 */
@Data
public class AcCalendarHeaderQueryCriteria implements Serializable {
    private Boolean enabled;
    private Long id;
    private Boolean defaultFlag;
}
