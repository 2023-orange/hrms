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
public class KpiAssessmentIndicatorsDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 主键
    private Long id;

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

    // 流水号
    private Long serialNumber;

    // KPI考核年度编号
    private Long kpiAnnualId;

    // 被考核组织ID
    private Long assessedDepartmentId;

    // 被考核组织ID
    private String assessedDepartmentName;

    // 考核类别
    private String examineType;

    // 目标维度
    private String targetDimension;

    // 关键绩效指标
    private String keyPerformanceIndicator;

    // 定义及计算方法
    private String define;

    // 权重
    private String weight;

    // 门槛值
    private String threshold;

    // 目标值
    private String targetValue;

    // 挑战值
    private String challengeValue;

    // 考核部门
    private Long examineDepartmentId;

    // 考核部门名称
    private String examineDepartmentName;

    // 考核人
    private Long examineEmployeeId;

    // 考核人工牌号
    private Long examineEmployeeWorkCard;

    // 考核名称
    private String examineEmployeeName;

    // 年份
    private String year;

    // 考核状态
    private String examineStatus;

    // 是否有效
    private Boolean enabledFlag;

    // 是否能够编辑
    private Boolean createByFlag;

    // 考核人姓名
    private String createByName;

    // 精确度
    private String dataAccuracy;

    // 数据类型
    private String dataType;

    private String assessedDeptName;

    private String assessedDepartment;

    private String assessedTeam;

    private String parentDepartment;
}
