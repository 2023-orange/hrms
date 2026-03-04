package com.sunten.hrms.re.vo;

import lombok.Data;

import java.util.List;

@Data
public class UsedByTrialApprovalDemandVo {
    private String demandCode;
    private Long id;
    private Long deptId;
    private String deptName;
    private String officeName;
    private String team;
    private List<UsedByTrialApprovalDemandJobVo> usedByTrialApprovalDemandJobVos;
    // 部门同期当年人数
    private Integer deptFirstCount;
    // 部门去年人数
    private Integer deptSecondCount;
    // 部门前年人数
    private Integer deptThirdCount;
    // 部门当年人数
    private Integer deptCurrentCount;
}
