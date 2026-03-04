package com.sunten.hrms.ac.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author ljw
 * @since 2020-09-17
 */
@Data
public class AcCalendarLineQueryCriteria implements Serializable {
    @NotBlank
    private Boolean enabled;
    @NotBlank
    private Long calendarHeaderId;

    private LocalDate beginDate;

    private LocalDate endDate;

}
