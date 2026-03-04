package com.sunten.hrms.pm.dto;

import com.sunten.hrms.pm.domain.PmItPermissions;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * @author liangjw
 * @since 2021-05-10
 */
@Data
public class PmPermissionLeaveApprovalQueryCriteria implements Serializable {
    private Long leaveApprovalId; // 离职申请ID

    private Boolean enabledFlag; // 有效标记

    private String belongType; // 所属类别
}
