package com.sunten.hrms.fnd.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author batan
 * @since 2019-12-19
 */
@Data
@NoArgsConstructor
public class FndJobQueryCriteria implements Serializable {

    private String name;

    private Long adminJobId;

    private Boolean enabled;

    private Boolean deleted = false;

    private Long deptId;

    private Set<Long> deptIds;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String jobCode;

    private Boolean adminFlag; // 查询管理层标记
    private String employeeName; // 按管理员姓名查询
}
