package com.sunten.hrms.td.dto;

    import java.math.BigDecimal;
    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.tool.dto.ToolLocalStorageDTO;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xukai
 * @since 2021-06-22
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdJobAuthenticationDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 人事ID
    private Long employeeId;

    // 工序
    private String process;

    // 认证岗位
    private String job;

    // 认证岗位类别
    private String authenticationLevel;

    // 岗位级别
    private String level;

    // 培训认证负责人
    private String surety;

    // 第一次通用考试成绩
    private BigDecimal firstGeneralGrade;

    // 第二次通用考试成绩
    private BigDecimal secondGeneralGrade;

    // 第三次通用考试成绩
    private BigDecimal threeGeneralGrade;

    // 第一次岗位理论考试成绩
    private BigDecimal firstTheoryGrade;

    // 第二次岗位理论考试成绩
    private BigDecimal secondTheoryGrade;

    // 第三次岗位理论考试成绩
    private BigDecimal threeTheoryGrade;

    // 第一次岗位实操评估成绩
    private BigDecimal firstOperationGrade;

    // 第二次岗位实操评估成绩
    private BigDecimal secondOperationGrade;

    // 第三次岗位实操评估成绩
    private BigDecimal threeOperationGrade;

    // 上岗认证证书发放日期
    private LocalDate credentialGrantDate;

    // 有效标记
    private Boolean enabledFlag;

    // 证书生效截止日期
    private LocalDate enabledTime;

    // 部门
    private String deptName;

    // 科室
    private String department;

    // 班组
    private String team;

    // 工号
    private String workCard;

    // 姓名
    private String name;

    private String attribute2;

    private String attribute3;

    private String attribute1;

    private Long id;

    private Long fileId;
    // 附件
    private ToolLocalStorageDTO storage;
}
