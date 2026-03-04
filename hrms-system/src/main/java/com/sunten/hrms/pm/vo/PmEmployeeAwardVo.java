package com.sunten.hrms.pm.vo;

import com.sunten.hrms.pm.domain.PmEmployeeAward;
import com.sunten.hrms.pm.domain.PmEmployeeAwardInterface;
import lombok.Data;

import java.util.List;

@Data
public class PmEmployeeAwardVo {
    Boolean reImportFlag;
    List<PmEmployeeAwardInterface> pmEmployeeAwardInterfaceList;
}
