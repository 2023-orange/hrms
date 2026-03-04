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

/**
 * <p>
 * it权限清单
 * </p>
 *
 * @author liangjw
 * @since 2021-05-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmItPermissions extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 权限负责部门id
     */
    @NotNull
    private Long permissionBelong;

    /**
     * 权限名称
     */
    @NotBlank
    private String permission;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


}
