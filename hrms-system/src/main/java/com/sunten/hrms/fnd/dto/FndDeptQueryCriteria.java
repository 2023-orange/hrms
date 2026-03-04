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
public class FndDeptQueryCriteria implements Serializable {
    private Set<Long> ids;

    private String name;

    private Boolean enabled;

    private Boolean deleted = false;

    private Long pid;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String deptCode;

    private Long adminJobId;

    private Boolean nullAdminJobId;

    private Boolean noUser;

    private Long deptId;
}
