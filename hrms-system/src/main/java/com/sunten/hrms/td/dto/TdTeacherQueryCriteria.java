package com.sunten.hrms.td.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Set;

/**
 * @author xukai
 * @since 2021-06-16
 */
@Data
public class TdTeacherQueryCriteria implements Serializable {
    private Long deptAllId; // 查部门及以下节点
    private Set<Long> deptIds; // 权限部门
    private Long employeeId; // 员工本人ID
    private String column; // 列名
    private String symbol; // 比较符
    private String value; // 查询值
    private String nameAndWork;
}
