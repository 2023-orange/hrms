package com.sunten.hrms.swm.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BatchSettingVo {
    // 所得期
    private String period;
    // 条目名
    private String entryName;
    // 数额
    private BigDecimal price;
    // 执行人
    private String name;
    // 执行时间
    private LocalDateTime datetime;
}
