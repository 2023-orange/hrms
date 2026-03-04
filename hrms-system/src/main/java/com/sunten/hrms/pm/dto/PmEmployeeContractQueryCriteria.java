package com.sunten.hrms.pm.dto;

import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

/**
 * @author batan
 * @since 2020-08-04
 */
@Data
@ToString(callSuper = true)
public class PmEmployeeContractQueryCriteria extends FndDynamicQueryBaseCriteria {
    private Long employeeId;
    private Set<Long> deptIds;
    private LocalDate selectTime;//合同结束时间
    private LocalDate today;
    private Boolean enabled;//合同生效标记

    private LocalDate beginDate;//起始时间
    private LocalDate endDate;//结束时间

    private Boolean enabledFlag; // 人员生效标记

    private Boolean leaveFlag;

    private Boolean newContractFlag;

}
