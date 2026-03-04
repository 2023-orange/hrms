package com.sunten.hrms.kpi.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhoujy
 * @since 2023-11-28
 */
@Getter
@Setter
@ToString(callSuper = true)
public class KpiAssessmentDimensionDeptDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 主键
    private Long id;

    // KPI考核维度id
    private Long assessmentDimensionId;

    // KPI部门树id
    private Long departmentTreeId;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 弹性域4
    private String attribute4;

    // 弹性域5
    private String attribute5;

    private String assessmentDimension;
}
