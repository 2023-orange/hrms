package com.sunten.hrms.swm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.math.BigDecimal;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmEmployeeDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 工牌号
    private String workCard;

    // 姓名
    private String name;

    // 银行账号
    private String bankAccount;

    // 开户行名称
    private String bankName;

    // 部门
    private String department;

    // 科室
    private String administrativeOffice;

    // 岗位
    private String station;

    // 员工类别
    private String employeeCategory;

    // 职级
    private String rank;

    // 技术职级
    private String technicalRank;

    // 技能级别
    private String skillLevel;

    // 职类
    private String category;

    // 职种
    private String job;

    // 职位
    private String position;

    // 职称
    private String title;

    // 学历
    private String education;

    // 入职时间
    private String entryTime;

    // 包干区分（1包干，0非包干）
    private Boolean divisionContractFlag;

    // 包干工资
    private BigDecimal lumpSumWage;

    // 生产区分（1生产，0非生产）
    private Boolean generationDifferentiationFlag;

    // 基本工资
    private BigDecimal basePay;

    // 岗位技能工资
    private BigDecimal postSkillSalary;

    // 目标绩效工资
    private BigDecimal targetPerformancePay;

    // 考核形式（月度考核、季度考核、无考核）
    private String accessmentForm;

    // 成本中心名称
    private String costCenter;

    // 成本中心号
    private String costCenterNum;

    // 服务部门
    private String serviceDepartment;

    // 班长津贴
    private BigDecimal squadLeaderAllowance;

    // 岗位补贴
    private BigDecimal postSubsidy;

    // 工龄津贴
    private BigDecimal seniorityAllowance;

    // 一孩补贴
    private BigDecimal oneChildSubsidy;

    // 高温补贴
    private BigDecimal highTemperatureSubsidy;

    // 安全累积奖
    private BigDecimal safetyAccumulationAward;

    // 扣除保险(个人)
    private BigDecimal personalDeductibles;

    // 扣除保险(公司)
    private BigDecimal companyDeductibles;

    // 扣除公积金（个人）
    private BigDecimal personalDeductAccumulationFund;

    // 扣除公积金（公司）
    private BigDecimal companyDeductAccumulationFund;

    // 交通津贴
    private BigDecimal transportationAllowance;

    // 是否在职
    private Boolean workFlag;

    // 弹性域
    private BigDecimal attribute1;

    // 弹性域
    private BigDecimal attribute2;

    // 弹性域
    private BigDecimal attribute3;

    // 弹性域
    private BigDecimal attribute4;

    // 弹性域
    private BigDecimal attribute5;

    // 弹性域
    private BigDecimal attribute6;

    // 弹性域
    private BigDecimal attribute7;

    // 弹性域
    private BigDecimal attribute8;

    // 弹性域
    private BigDecimal attribute9;

    // 弹性域
    private BigDecimal attribute10;

    // 人事模块的员工id
    private Long employeeId;

    private String team;

    private Boolean managerFlag;

    private String type;

    private String updateByStr;

    private Integer pmTransferRequestId;


}
