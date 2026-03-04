package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Set;

/**
 * @author xukai
 * @since 2021-04-19
 */
@Data
public class PmMedicalJobQueryCriteria implements Serializable {
    private Long deptId; // 部门id
    // deptName,officeName 与deptId结合使用，达成查询选中部门及以下的节点信息
    private String deptName; // 部门名称
    private String officeName; // 科室名称
    private Set<Long> deptIds; // 权限部门
    private Long jobId; // 岗位id

    private String colName; // 列表
    private String symbol; // 比较符
    private String value; // 查询值
    private Long deptAllId; // 查部门及以下节点
}
