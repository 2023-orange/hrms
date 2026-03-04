package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author liangjw
 * @since 2021-05-10
 */
@Data
public class PmItPermissionsQueryCriteria implements Serializable {

    private Boolean enabledFlag; // 有效标识

    private Long permissionBelong; // 权限所属id


}
