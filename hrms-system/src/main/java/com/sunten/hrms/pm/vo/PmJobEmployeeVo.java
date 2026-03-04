package com.sunten.hrms.pm.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PmJobEmployeeVo {
    private String name;
    private String workCard;
    private Boolean partTimeFlag;
    private Boolean leaveFlag;
    private String gender;
}
