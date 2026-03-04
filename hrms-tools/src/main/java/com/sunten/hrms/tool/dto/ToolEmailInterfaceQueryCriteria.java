package com.sunten.hrms.tool.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batan
 * @since 2020-10-30
 */
@Data
public class ToolEmailInterfaceQueryCriteria implements Serializable {
    private Long id;

    // 模糊
    private String blurry;

    private String status;

    private String sendTo;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime planStartTime;

    private LocalDateTime planEndTime;

}
