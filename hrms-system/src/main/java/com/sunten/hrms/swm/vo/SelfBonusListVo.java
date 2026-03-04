package com.sunten.hrms.swm.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SelfBonusListVo {
    private String bonusName;

    private String month;

    private LocalDate releaseTime;

    private BigDecimal payableAmount;

    private BigDecimal amountPreTax;

    private BigDecimal deductIncomeTax;

    private BigDecimal amountAfterTax;

}
