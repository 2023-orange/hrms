package com.sunten.hrms.ac.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @atuthor xukai
 * @date 2021/1/6 15:09
 */
@Data
public class AcVacateQueryCriteria implements Serializable {
    private String reqCode;

    private LocalDate exceptionDate;// 异常日期

    private String workCard; // 工牌号
}
