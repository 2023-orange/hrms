package com.sunten.hrms.fnd.domain;

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
public class FndUsersRoles extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
//    @TableId(value = "user_id", type = IdType.NONE)
    @NotNull
    private Long userId;

    /**
     * 角色ID
     */
//    @TableId(value = "role_id", type = IdType.NONE)
    @NotNull
    private Long roleId;


}
