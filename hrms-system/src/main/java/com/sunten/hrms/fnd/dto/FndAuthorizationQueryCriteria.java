package com.sunten.hrms.fnd.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * @author xukai
 * @since 2021-01-29
 */
@Data
public class FndAuthorizationQueryCriteria implements Serializable {
    // 被授权人ID
    private Long toEmployeeId;

    // 授权人ID
    private Long byEmployeeId;

    // 生效标识
    private Boolean enableFlag;

    // 权限类型
    private String authorizationType;

    // 起始日期
    private LocalDate beginDate;

    // 截止日期
    private LocalDate endDate;

    // 授权人员empId集合
    private Set<Long> byEmployeeIds;


}
