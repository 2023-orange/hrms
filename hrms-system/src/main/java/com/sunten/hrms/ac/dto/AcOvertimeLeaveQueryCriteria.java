package com.sunten.hrms.ac.dto;

import com.sunten.hrms.fnd.dto.AdvancedCriteriaQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

/**
 * @author liangjw
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AcOvertimeLeaveQueryCriteria extends AdvancedCriteriaQuery implements Serializable {
    // 时间查询时使用
    private LocalDate date;

    // 部门查询
    private Long deptId;

    // 部门查询
    private Set<Long> deptIds;

    private Long employeeId;

    // 姓名查询
    private String name;

    // 工号查询
    private String workCard;

    // 数某月已导入数量时使用
    private LocalDate month;

    // 失效有效
    private Boolean enabled;

    // 计算值
    private BigDecimal culTime;
    // 开始时间
    private LocalDate dateFrom;
    // 结束时间
    private LocalDate dateTo;

}
