package com.sunten.hrms.swm.domain;

import lombok.Data;

import java.util.List;

@Data
public class SwmEmployeeExcel {
    private List<SwmEmployeeInterface> swmEmployeeInterfaces;
    private Boolean reImportFlag;
}
