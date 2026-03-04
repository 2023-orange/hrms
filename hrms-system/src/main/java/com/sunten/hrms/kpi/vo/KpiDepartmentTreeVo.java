package com.sunten.hrms.kpi.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.kpi.domain.KpiAssessmentDimensionDept;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * KPI部门树表
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class KpiDepartmentTreeVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long departmentTreeId;

    /**
     * 是否为考核部门
     */
    private Boolean assessmentDepartmentFlag;

    /**
     * 是否为被考核部门
     */
    private Boolean assessedDepartmentFlag;

    private Long[] kpiAssessmentDimensionDept;

}
