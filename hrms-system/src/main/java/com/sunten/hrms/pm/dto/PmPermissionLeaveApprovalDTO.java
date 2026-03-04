package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.pm.domain.PmItPermissions;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-05-10
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmPermissionLeaveApprovalDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 离职审批id
    private Long leaveApprovalId;

    // IT权限id
    private Long permissionId;

    // 停用日期
    private String stopDate;

    // 有效标记
    private Boolean enabledFlag;

    private Long id;

    private PmItPermissions pmItPermissions; // 权限

}
