package com.sunten.hrms.fnd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Set;

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
public class FndUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 头像
     */
    //private Long avatarId;
    @TableField(exist = false)
    private FndUserAvatar userAvatar;

    /**
     * 用户名
     */
    @NotBlank
    private String username;

    private String description;

    private String salt;

    /**
     * 邮箱
     */
    @NotBlank
    @Pattern(regexp = "([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}", message = "格式错误")
    private String email;

    /**
     * 密码
     */
    private String password;


    @NotBlank
    private String phone;

    /**
     * 最后修改密码的日期
     */
    private LocalDateTime lastPasswordResetTime;

    @TableField(exist = false)
    private Set<FndRole> roles;


    /**
     * 状态：1启用、0禁用
     */
    @NotNull
    private Boolean enabledFlag;

    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;


    @TableField(exist = false)
    private PmEmployee employee;

    @TableField(exist = false)
    private FndDept dept;

    @TableField(exist = false)
    private FndJob job;
}
