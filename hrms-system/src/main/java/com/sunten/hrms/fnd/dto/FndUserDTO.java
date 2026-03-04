package com.sunten.hrms.fnd.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sunten.hrms.base.BaseDTO;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author batan
 * @since 2019-12-19
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndUserDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // ID
    @ApiModelProperty(hidden = true)
    private Long id;

    // 头像
    private String avatar;

    // 用户名
    private String username;

    private String description;

    private String salt;

    // 邮箱
    private String email;

    // 密码
    @JsonIgnore
    private String password;

    private String phone;

    // 最后修改密码的日期
    private LocalDateTime lastPasswordResetTime;

    @ApiModelProperty(hidden = true)
    private Set<FndRoleSmallDTO> roles;

    @ApiModelProperty(hidden = true)
    private FndJobSmallDTO job;

    private FndDeptSmallDTO dept;

    // 状态：1启用、0禁用
    private Boolean enabledFlag;

    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;

    private PmEmployeeDTO employee;
}
