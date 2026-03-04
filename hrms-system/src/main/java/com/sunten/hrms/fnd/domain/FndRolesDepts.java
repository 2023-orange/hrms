package com.sunten.hrms.fnd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 *
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndRolesDepts extends BaseEntity {

    private static final long serialVersionUID = 1L;

//    @TableId(value = "role_id", type = IdType.NONE)
    @NotNull
    private Long roleId;

    @TableField(exist=false)
    private FndRole role;

//    @TableId(value = "dept_id", type = IdType.NONE)
    @NotNull
    private Long deptId;


    @TableField(exist=false)
    private FndDept dept;

}
