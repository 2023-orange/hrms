package com.sunten.hrms.tool.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batan
 * @since 2019-12-25
 */
@Data
public class ToolLocalStorageQueryCriteria implements Serializable {

    // 模糊
    private String blurry;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
