package com.sunten.hrms.fnd.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batan
 * @since 2019-5-22
 */
@Data
public class FndLogSmallDTO implements Serializable {

    // 描述
    private String description;

    // 请求ip
    private String requestIp;

    // 请求耗时
    private Long time;

    private String address;

    private String browser;

    // 创建日期
    private LocalDateTime createTime;
}
