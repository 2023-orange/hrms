package com.sunten.hrms.kpi.domain;

import lombok.Data;

import java.util.Set;

@Data
public class KpiInterfaceQueryCriteria {
    private Long groupId;
    private String dataStatus;
    private Set<Long> groupIds;

}
