package com.sunten.hrms.re.vo;

import lombok.Data;

@Data
public class UsedByTrialApprovalDemandJobVo {
    private Long id;
    private Long jobId;
    private String jobName;
    private Integer quantity;
    private Integer inUsedQuantity;
    private Integer passQuantity;
    private Integer agreeNum;
    // 岗位同期当年人数
    private Integer jobFirstCount;
    // 岗位同期去年人数
    private Integer jobSecondCount;
    // 岗位同期前年人数
    private Integer jobThirdCount;
    // 岗位当前人数
    private Integer jobCurrentCount;
}
