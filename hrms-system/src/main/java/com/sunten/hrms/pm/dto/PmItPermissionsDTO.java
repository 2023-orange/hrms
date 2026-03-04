package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
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
public class PmItPermissionsDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 权限负责部门id
    private Long permissionBelong;

    // 权限名称
    private String permission;

    // 有效标记
    private Boolean enabledFlag;

    private Long id;


}
