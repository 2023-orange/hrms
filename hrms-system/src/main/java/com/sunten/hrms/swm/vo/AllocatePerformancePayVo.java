package com.sunten.hrms.swm.vo;

import com.sunten.hrms.swm.domain.SwmFloatingWageInterface;
import lombok.Data;

import java.util.List;

@Data
public class AllocatePerformancePayVo {
    List<SwmFloatingWageInterface> swmFloatingWageInterfaces;
    Boolean reImportFlag;
}
