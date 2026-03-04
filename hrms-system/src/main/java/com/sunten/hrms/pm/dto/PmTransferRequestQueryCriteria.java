package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Set;

/**
 * @author xukai
 * @since 2021-05-24
 */
@Data
public class PmTransferRequestQueryCriteria implements Serializable {
    private Long deptAllId; // 查部门及以下节点
    private Set<Long> deptIds; // 权限部门
    private Long employeeId; // 员工ID
    private String column; // 列名
    private String symbol; // 比较符
    private String value; // 查询值
    private Boolean submitFlag; // 提交标识
    private String transferType; // 调动类型
}
