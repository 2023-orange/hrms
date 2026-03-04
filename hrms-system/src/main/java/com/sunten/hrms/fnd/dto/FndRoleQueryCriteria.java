package com.sunten.hrms.fnd.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batan
 * @since 2019-12-19
 */
@Data
public class FndRoleQueryCriteria implements Serializable {

    // 多字段模糊
    private String blurry;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String permission;
}
