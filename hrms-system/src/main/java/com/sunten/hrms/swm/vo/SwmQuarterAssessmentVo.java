package com.sunten.hrms.swm.vo;

import com.sunten.hrms.swm.domain.SwmQuarterlyAssessment;
import com.sunten.hrms.swm.domain.SwmQuarterlyAssessmentInterface;
import lombok.Data;

import java.util.List;

@Data
public class SwmQuarterAssessmentVo {
    private Boolean reImportFlag;
    List<SwmQuarterlyAssessmentInterface> swmQuarterlyAssessmentInterfaces;
}
