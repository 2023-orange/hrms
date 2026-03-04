package com.sunten.hrms.fnd.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * @author batan
 * @since 2019-12-19
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndRoleDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // ID
    private Long id;

    // 名称
    private String name;

    // 备注
    private String remark;

    private String dataScope;

    private Integer level;

    private String permission;

    private Set<FndMenuDTO> menus;

    private Set<FndDeptDTO> depts;

}
