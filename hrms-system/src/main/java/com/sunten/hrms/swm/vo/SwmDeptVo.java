package com.sunten.hrms.swm.vo;

import lombok.Data;

import java.util.List;

@Data
public class SwmDeptVo {
    // 部门代号， 作用于前台绑定key
    private String DeptCode;
    // 部门名， value
    private String DeptName;
    // 子
    private List<SwmDeptVo> childSet;
}
