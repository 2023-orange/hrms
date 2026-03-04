package com.sunten.hrms.kpi.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
public class KpiDepartmentTree extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 年份
     */

    private String year;

    /**
     * KPI考核年度编号
     */

    private Long kpiAnnualId;

    /**
     * 部门编号
     */

    private Long deptId;

    /**
     * 部门名称
     */

    private String deptName;

    /**
     * 父级节点id
     */

    private Long parentId;

    /**
     * 部门层级
     */
    private String deptLevel;

    /**
     * 节点序号
     */

    private Integer deptSequence;

    /**
     * 序号
     */
    private Integer sequence;

    /**
     * 部、部门（根据架构树扩展）
     */
    private Long extDeptId;

    /**
     * 科室（根据架构树扩展）
     */
    private Long extDepartmentId;

    /**
     * 班组（根据架构树扩展）
     */
    private Long extTeamId;

    /**
     * 管理岗ID
     */
    private Long adminJobId;

    /**
     * 是否为考核部门
     */
    private Boolean assessmentDepartmentFlag;

    /**
     * 是否为被考核部门
     */
    private Boolean assessedDepartmentFlag;

    /**
     * 资料填写人
     */
    private Long infoWorkCard;

    /**
     * 弹性域1
     */
    private String attribute1;

    /**
     * 弹性域2
     */
    private String attribute2;

    /**
     * 弹性域3
     */
    private String attribute3;

    /**
     * 弹性域4
     */
    private String attribute4;

    /**
     * 弹性域5
     */
    private String attribute5;

    private Long[] kpiAssessmentDimensionDept;

    // 人员对照表
    private String employees;

    // 考核维度对照表
    private String assessmentDimensions;
}
