package com.sunten.hrms.ac.vo;

import com.sunten.hrms.ac.domain.AcOvertimeLeaveInterface;
import lombok.Data;

import java.util.List;

@Data
public class AcOvertimeImportVo {
    List<AcOvertimeLeaveInterface> acOvertimeLeaveInterfaces;

    Boolean reImportFlag;
}
