package com.sunten.hrms.td.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString(callSuper = true)
public class AgreementExcelVo {
    private String trainingName;

    private String trainingAddress;

    private Double trainingTimeQuantity;

    private LocalDate beginDate;

    private LocalDate endDate;

    private BigDecimal planMoney;

    private BigDecimal trainingMoney;

    private String name;

    private String workCard;

    private String idNumber;

    private Double serviceYear;

    private LocalDate serviceBeginDate;

    private LocalDate serviceEndDate;

    private Long planId;

    private Long id;

    private String checkMethod;
}
