package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * <p>
 * 离职申请与IT权限关联表
 * </p>
 *
 * @author liangjw
 * @since 2021-05-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmPermissionLeaveApproval extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 离职审批id
     */
    @NotNull
    private Long leaveApprovalId;

    /**
     * IT权限id
     */
    @NotNull
    private Long permissionId;

    /**
     * 停用日期
     */
    private LocalDate stopDate;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private PmItPermissions pmItPermissions; // 权限


}
