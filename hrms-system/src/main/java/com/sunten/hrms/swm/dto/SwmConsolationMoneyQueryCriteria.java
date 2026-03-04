package com.sunten.hrms.swm.dto;

import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.AdvancedCriteriaQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author liangjw
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SwmConsolationMoneyQueryCriteria extends AdvancedCriteriaQuery implements Serializable {

    private Long deptAllId;

    private Set<Long> deptIds;

    private Boolean enabledFlag;

    private Long employeeId;

    private Long userId;

    private Set<Long> adminDeptIds;

    private Boolean adminFlag;

    private Boolean allFlag;

    private List<Long> ids;

    private LocalDate releasedTime;

    private LocalDate releasedTimeStart;

    private LocalDate releasedTimeEnd;

}
