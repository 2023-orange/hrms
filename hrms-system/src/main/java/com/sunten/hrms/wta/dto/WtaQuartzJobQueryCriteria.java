package com.sunten.hrms.wta.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batan
 * @since 2019-12-23
 */
@Data
public class WtaQuartzJobQueryCriteria implements Serializable {

    private String jobName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
