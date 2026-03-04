package com.sunten.hrms.pm.vo;

import lombok.Data;

@Data
public class PmLeaderVo {
    private String currentSupervisor; // 上级主管
    private String currentManager; // 上级经理
    private Boolean leaderFlag; // 管理岗标记
}
