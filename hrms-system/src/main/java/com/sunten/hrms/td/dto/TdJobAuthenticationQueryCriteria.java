package com.sunten.hrms.td.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Set;

/**
 * @author xukai
 * @since 2021-06-22
 */
@Data
public class TdJobAuthenticationQueryCriteria implements Serializable {
    private Long deptAllId; // 查部门及以下节点
    private Set<Long> deptIds; // 权限部门
    private Long employeeId; // 员工ID
    private String column; // 列名
    private String symbol; // 比较符
    private String value; // 查询值
    /**
     * 部门id，只查询本部门下的一级节点信息
     */
    private Long deptId;
}
