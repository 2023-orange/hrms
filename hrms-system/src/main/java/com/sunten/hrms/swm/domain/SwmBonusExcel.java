package com.sunten.hrms.swm.domain;

import lombok.Data;

import java.util.List;

@Data
public class SwmBonusExcel {
    private List<SwmBonusPaymentInterface> swmBonusPaymentInterfaces;
    private Boolean reImportFlag;
}
