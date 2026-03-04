package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 离职审批表
 * </p>
 *
 * @author liangjw
 * @since 2021-05-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmLeaveApproval extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     */
    @NotNull
    private Long employeeId;

    /**
     * 离职人员填写表单的专用判定类别
     */
    @NotBlank
    private String employeeClass;

    /**
     * 离职原因
     */
    @NotBlank
    private String leaveReason;

    /**
     * 区域（销售）
     */
    private String deptAddress;

    /**
     * 联系方式及常住地址（销售）
     */
    private String photoAndAddress;

    /**
     * 工作日加班工时（非销售）
     */
    private BigDecimal workOvertime;

    /**
     * 休息日加班工时（非销售）
     */
    private BigDecimal restOvertime;

    /**
     * 调休工时（非销售）
     */
    private BigDecimal offHours;

    /**
     * 个人申请离职日期
     */
    private LocalDate selfLeaveDate;

    /**
     * 准许离职日期
     */
    private LocalDate approveLeaveDate;

    /**
     * 工卡是否上交
     */
    @NotNull
    private Boolean cardFlag;

    /**
     * 未上交原因
     */
    private String notSubmitReason;

    /**
     * 最后离职日期
     */
    private LocalDate lastLeaveDate;

    /**
     * 人资人事管理
     */
    private String pmAdmin;

    /**
     * 审批意见
     */
    private String approvalResult;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;

    /**
     * OA单号
     */
    private String oaOrder;

    /**
     * 审批日期
     */
    private LocalDate approvalDate;

    /**
     * 申请状态
     */
    @NotBlank
    private String approvalStatus;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private LocalDate entryTime; // 入职时间

    private LocalDate contractStartTime; // 合同开始时间

    private LocalDate contractEndTime; // 合同结束时间

    private String deptName;

    private String department;

    private String team;

    private String jobName;

    private String name;

    private String workCard;

    private String idNumber;

    private List<PmPermissionLeaveApproval> pmPermissionLeaveApprovals;

    private String currentNode;

    private String currentPerson;

    private String empTele;

    private String seniorManagerWorkCard; // 高级经理工号

    private String targetDeptName;

    private Long deptId;

    private Boolean deleteFlag;

    private Long jobId;

    private String deduction;

    private String medicalType;

}
