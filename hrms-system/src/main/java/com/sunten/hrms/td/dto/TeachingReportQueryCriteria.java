package com.sunten.hrms.td.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @atuthor xukai
 * @date 2021/7/5 15:22
 */
@Data
public class TeachingReportQueryCriteria implements Serializable {
    private String beginDate;
    private String endDate;
}
