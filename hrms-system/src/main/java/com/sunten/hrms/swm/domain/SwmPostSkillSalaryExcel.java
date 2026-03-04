package com.sunten.hrms.swm.domain;

import lombok.Data;

import java.util.List;

@Data
public class SwmPostSkillSalaryExcel {
    private List<SwmPostSkillSalaryInterface> swmPostSkillSalaryInterfaces;
    private Boolean reImportFlag;
}
