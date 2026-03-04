package com.sunten.hrms.swm.domain;

import lombok.Data;

import java.util.List;

@Data
public class SwmPersonalIncomeTaxExcel {
    private List<SwmPersonalIncomeTaxInterface> swmPersonalIncomeTaxInterfaces;
    private Boolean reImportFlag;
}
