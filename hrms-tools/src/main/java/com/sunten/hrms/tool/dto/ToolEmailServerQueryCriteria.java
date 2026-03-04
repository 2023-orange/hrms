package com.sunten.hrms.tool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author batan
 * @since 2020-11-02
 */
@Data
public class ToolEmailServerQueryCriteria implements Serializable {
    // 模糊
    private String blurry;

    private Boolean enabled;

    private String fromUser;
}
