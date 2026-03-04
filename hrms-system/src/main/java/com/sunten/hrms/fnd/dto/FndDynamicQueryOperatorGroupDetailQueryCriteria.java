package com.sunten.hrms.fnd.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author batan
 * @since 2022-07-26
 */
@Data
public class FndDynamicQueryOperatorGroupDetailQueryCriteria implements Serializable {
    private Long groupId;
    private Boolean enabled;
}
