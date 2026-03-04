package com.sunten.hrms.fnd.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batan
 * @since 2019-12-23
 */
@Data
public class FndLogQueryCriteria implements Serializable {

    // 多字段模糊
    private String blurry;

    private String logType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
