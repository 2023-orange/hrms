package com.sunten.hrms.cm.dto;

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
 * @since 2022-03-24
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CmDetailQueryCriteria extends FndDynamicQueryBaseCriteria {
    private Boolean enabledFlag;
    private Boolean leaveFlag;
    private Set<Long> deptIds;
    private Integer year;
    private Long deptId;
    private Long employeeId;
    private Boolean exportFlag;
    private Long deptAllId;

}
