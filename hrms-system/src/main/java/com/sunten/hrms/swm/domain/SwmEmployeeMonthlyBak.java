package com.sunten.hrms.swm.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 薪酬员工信息每月备份表
 * </p>
 *
 * @author liangjw
 * @since 2021-09-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmEmployeeMonthlyBak extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 工牌号
     */
    @NotBlank
    private String workCard;

    /**
     * 姓名
     */
    @NotBlank
    private String name;

    /**
     * 银行账号
     */
    @NotBlank
    private String bankAccount;

    /**
     * 开户行名称
     */
    private String bankName;

    /**
     * 部门
     */
    @NotBlank
    private String department;

    /**
     * 科室
     */
    private String administrativeOffice;

    /**
     * 岗位
     */
    private String station;

    /**
     * 员工类别
     */
    @NotBlank
    private String employeeCategory;

    /**
     * 职级
     */
    private String rank;

    /**
     * 技术职级
     */
    private String technicalRank;

    /**
     * 技能级别
     */
    private String skillLevel;

    /**
     * 职类
     */
    private String category;

    /**
     * 职种
     */
    private String job;

    /**
     * 职位
     */
    private String position;

    /**
     * 职称
     */
    private String title;

    /**
     * 学历
     */
    private String education;

    /**
     * 入职时间
     */
    private LocalDate entryTime;

    /**
     * 包干区分（1包干，0非包干）
     */
    @NotNull
    private Boolean divisionContractFlag;

    /**
     * 包干工资
     */
    private BigDecimal lumpSumWage;

    /**
     * 生产区分（1生产，0非生产）
     */
    @NotNull
    private Boolean generationDifferentiationFlag;

    /**
     * 基本工资
     */
    private BigDecimal basePay;

    /**
     * 岗位技能工资
     */
    private BigDecimal postSkillSalary;

    /**
     * 目标绩效工资
     */
    private BigDecimal targetPerformancePay;

    /**
     * 考核形式（月度考核、季度考核、无考核）
     */
    @NotBlank
    private String accessmentForm;

    /**
     * 成本中心名称
     */
    @NotBlank
    private String costCenter;

    /**
     * 成本中心号
     */
    @NotBlank
    private String costCenterNum;

    /**
     * 服务部门
     */
    private String serviceDepartment;

    /**
     * 班长津贴
     */
    private BigDecimal squadLeaderAllowance;

    /**
     * 岗位补贴
     */
    private BigDecimal postSubsidy;

    /**
     * 工龄津贴
     */
    private BigDecimal seniorityAllowance;

    /**
     * 一孩补贴
     */
    private BigDecimal oneChildSubsidy;

    /**
     * 高温补贴
     */
    private BigDecimal highTemperatureSubsidy;

    /**
     * 安全累积奖
     */
    private BigDecimal safetyAccumulationAward;

    /**
     * 扣除保险(个人)
     */
    @NotNull
    private BigDecimal personalDeductibles;

    /**
     * 扣除保险(公司)
     */
    @NotNull
    private BigDecimal companyDeductibles;

    /**
     * 扣除公积金（个人）
     */
    @NotNull
    private BigDecimal personalDeductAccumulationFund;

    /**
     * 扣除公积金（公司）
     */
    @NotNull
    private BigDecimal companyDeductAccumulationFund;

    /**
     * 交通津贴
     */
    @NotNull
    private BigDecimal transportationAllowance;

    /**
     * 备份日期
     */
    @NotNull
    private LocalDate bakDate;

    /**
     * 弹性域
     */
    private BigDecimal attribute1;

    /**
     * 弹性域
     */
    private BigDecimal attribute2;

    /**
     * 弹性域
     */
    private BigDecimal attribute3;

    /**
     * 弹性域
     */
    private BigDecimal attribute4;

    @NotNull
    private Long pmEmployeeId;

    @NotNull
    private Long swmEmployeeId;

    @NotNull
    private Boolean managerFlag;

    private String team;

    private String incomePeriod;


}
