package com.sunten.hrms.fnd.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2019-12-19
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndRolesMenusDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 菜单ID
    private Long menuId;

    // 角色ID
    private Long roleId;


}
