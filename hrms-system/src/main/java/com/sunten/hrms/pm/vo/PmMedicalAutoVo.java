package com.sunten.hrms.pm.vo;

import com.sunten.hrms.pm.domain.PmMedicalLine;
import lombok.Data;

@Data
public class PmMedicalAutoVo {
    private PmMedicalLine PmMedicalLine;
    private String workAndHazard;
}
