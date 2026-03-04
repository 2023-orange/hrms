package com.sunten.hrms.fnd.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author batan
 * @since 2022-07-29
 */
@Data
public class FndDynamicQueryGroupDetailQueryCriteria implements Serializable {
    private Long groupId;
    private String groupName;
    private Boolean enabled;
    private String blurry;

}
