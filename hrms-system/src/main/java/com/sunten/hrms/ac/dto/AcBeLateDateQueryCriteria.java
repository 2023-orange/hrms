package com.sunten.hrms.ac.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author xukai
 * @since 2021-07-08
 */
@Data
public class AcBeLateDateQueryCriteria implements Serializable {
    private String year; // 年份
    private Integer month; // 月份
    private Boolean enabledFlag; // 生效标识
    private LocalDate beginDate; // 开始日期
    private LocalDate endDate; // 开始日期
    private Boolean lateFlag; // 1早上上班延迟、0下午下班提前的标记。
}
