package com.sunten.hrms.td.vo;

import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Date;

@Data
@ToString(callSuper = true)
public class DeptPlanExcelVo extends BaseEntity {

    private String deptName;

    private String department;

    private String team;

    private Integer deptMenberCount; // 部门人数

    private Integer planMenber; // 计划人数

    private Double duration; // 培训时长

    private Double grade; // 分数

    private Double evaluate; // 满意度

    private Integer mainPlanCount; // 发起培训项目数

    private Integer mainPlanPassCount; // 已实施项目数

    private Double planCoverage; // 培训覆盖率

    private Double PerCapitaDuration; // 人均培训时长

    private Double PerCapitaEvaluate; // 人均满意度

    private LocalDate beginDate;

    private LocalDate endDate;

    private String dateStr;

    private Long id;
}
