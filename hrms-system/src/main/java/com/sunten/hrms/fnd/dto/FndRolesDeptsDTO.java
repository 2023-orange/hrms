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
public class FndRolesDeptsDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long roleId;

    private Long deptId;


}
