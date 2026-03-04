package com.sunten.hrms.kpi.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author zhoujy
 * @since 2023-11-27
 */
@Data
public class KpiDepartmentTreeQueryCriteria implements Serializable {

    // 年份
    String year;

    // 是否为考核部门
    private Boolean assessmentDepartmentFlag;

    // 是否为被考核部门
    private Boolean assessedDepartmentFlag;

    String name;
}
