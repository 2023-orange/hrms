package com.sunten.hrms.fnd.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author batan
 * @since 2019-12-19
 */
@Data
public class FndUserQueryCriteria implements Serializable {

    private Long id;

    private Set<Long> deptIds;

    // 多字段模糊
    private String blurry;

    private Boolean enabled;

    private Long deptId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long employeeId;
}
