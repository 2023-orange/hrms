package com.sunten.hrms.pm.dto;

import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author batan
 * @since 2020-08-04
 */
@Data
@ToString(callSuper = true)
public class PmEmployeeLeaveofficeQueryCriteria extends FndDynamicQueryBaseCriteria  {
    private Long employeeId;
    private Boolean enabled;

    private String deptName;
    private List<Long> employeeIds;
    private Set<Long> deptIds;
    //起始时间，与结束时间相匹配，二者均不为空才启用该条件
    private LocalDate leaveTimeStart;
    //结束时间
    private LocalDate leaveTimeEnd;

}
