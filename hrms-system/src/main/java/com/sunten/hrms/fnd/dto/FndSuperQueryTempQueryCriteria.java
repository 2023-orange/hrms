package com.sunten.hrms.fnd.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * @author liangjw
 * @since 2021-08-19
 */
@Data
@ToString(callSuper = true)
public class FndSuperQueryTempQueryCriteria extends FndDynamicQueryBaseCriteria {
    private String queryValue;

    // 创建标记
    private Boolean createFlag;

    private Long searchUserId;

    private String groupName;

    private String queryType = "CROSS";

}
