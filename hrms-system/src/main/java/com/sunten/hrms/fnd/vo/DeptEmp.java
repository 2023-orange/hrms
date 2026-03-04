package com.sunten.hrms.fnd.vo;

import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.domain.FndDeptSnapshot;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;

@Data

public class DeptEmp {
    private static final long serialVersionUID = 1L;

   private  FndDept fndDept;

    private PmEmployee employee;

    private FndUser fndUser;

    private String email;

    private String deptName;

    private Long deptId;

    private Long employeeId;
}
