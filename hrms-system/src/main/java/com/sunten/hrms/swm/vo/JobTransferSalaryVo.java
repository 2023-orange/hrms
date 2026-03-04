package com.sunten.hrms.swm.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JobTransferSalaryVo {
    private BigDecimal fromSalary;

    private String fromSalaryStr;
}
