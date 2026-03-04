package com.sunten.hrms.swm.dto;

import lombok.Data;

import java.util.Set;

@Data
class SwmSalaryInterfaceQueryCriteria {
    private Long groupId;
    private String dataStatus;
    private Set<Long> groupIds;

}
