package com.sunten.hrms.swm.dto;

import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.AdvancedCriteriaQuery;
import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SwmEmployeeQueryCriteria extends FndDynamicQueryBaseCriteria {

    private String nameOrWorkCard;

    private Boolean workFlag;

    private String deptCode;

    private String department;

    private String administrativeOffice;

    private Boolean managerFlag;


    private Boolean enabledFlag;

    private String workCard;

    private String type;

    private Long seId;

    private Long deptAllId; // 查部门及以下节点
    private Set<Long> deptIds; // 权限部门

    private Long employeeId;
}
