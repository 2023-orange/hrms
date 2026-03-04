package com.sunten.hrms.pm.dto;

import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import lombok.Data;
import lombok.ToString;

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
public class PmEmployeeEntryQueryCriteria extends FndDynamicQueryBaseCriteria {
    private Long employeeId;
    private Set<Long> deptIds;
    // 试用期生效标记
    private Boolean enabled;

    // 人员生效标记
    private Boolean enabledFlag;
    /**
     * 条件1、当为all（空） 时，查询所有
     * 条件2、当为今天的日期时，查询所有试用期结束时间晚于今天的信息
     * 条件3、当不为今天的日期时，查询所有试用期结束时间在今天至selectTime的信息
     */
    private LocalDate selectTime;

    private String selectTimeType;//辅助参数，根据selectTime确定，满足条件1时为空，满足条件2时为today,满足条件3时为dayAfter

    private Boolean leaveFlag;

    private LocalDate beginDate;// 转正日期起始范围日期
    private LocalDate endDate; // 转正日期结束范围日期

}
