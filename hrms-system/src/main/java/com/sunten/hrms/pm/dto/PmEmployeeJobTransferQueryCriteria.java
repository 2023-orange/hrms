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
public class PmEmployeeJobTransferQueryCriteria extends FndDynamicQueryBaseCriteria {
    private Long employeeId;
    private Set<Long> deptIds;
    // 岗位调动
    private Boolean enabled;
    // 人员有效标记
    private Boolean enabledFlag;
//    private String colName;
//    private String symbol;
//    private String value;

    //起始时间，与结束时间相匹配，均不为空时才开始查询
    private LocalDate transferStartTime;
    //结束时间
    private LocalDate transferEndTime;

    private LocalDate taskTransferStartTime;

    private LocalDate taskTransferEndTime;

    private List<String> states;

    private Boolean leaveFlag;
    private Boolean expInit;

    private Boolean transferType; // 调动类型，true代表非初始调动，false代表初始调动

    private String transferForm; // 调动类型

//    // 解析后的高级查询内容
//    private List<AdvancedQuery> advancedQuerys;
//    // 前台传过来时的高级查询内容的JSON串
//    private String advancedQuerysStr;
}
