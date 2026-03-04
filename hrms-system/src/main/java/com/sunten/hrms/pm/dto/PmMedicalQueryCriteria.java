package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Set;

/**
 * @author xukai
 * @since 2021-04-07
 */
@Data
public class PmMedicalQueryCriteria implements Serializable {
    private Set<Long> deptIds; // 部门权限
    private Long deptAllId; // 部门及以下ID
    private String column; // 列名
    private String symbol; // 符号
    private String value; // 查询值
    private Boolean enabledFlag;// 生效标记
    private Boolean submitFlag;// 提交标识

    private Long employeeId;
}
