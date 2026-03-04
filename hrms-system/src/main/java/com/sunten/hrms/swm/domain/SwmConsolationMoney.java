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
 * 慰问金表
 * </p>
 *
 * @author liangjw
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmConsolationMoney extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 人员id
     */
    @NotNull
    private Long employeeId;

    /**
     * 慰问金类别
     */
    @NotBlank
    private String consolationMoneyType;

    /**
     * 申请日期
     */
    private LocalDate applicationDate;

    /**
     * 申请金额
     */
    @NotNull
    private BigDecimal applicationMoney;

    /**
     * 逝世亲属及称谓(类别为丧事时填写)
     */
    private String relativesDied;

    /**
     * 申请单号
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
    private String status;

    /**
     * 审批状态
     */
    private String approvalStatus;

    /**
     * 当前审批节点
     */
    private String currentNode;

    /**
     * 当前审批人
     */
    private String currentPerson;

    /**
     * 慰问金导出标记
     */
    @NotNull
    private Boolean exportFlag;

    /**
     * 实际发放金额
     */
    private BigDecimal releasedMoney;

    /**
     * 发放标记
     */
    @NotNull
    private Boolean releasedFlag;

    /**
     * 发放日期
     */
    private LocalDate releasedTime;

    private String releasedTimeStr;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private String name;

    private String workCard;

    private String deptName;

    private String departmentName;

    private String teamName;

    // 标记为1的即有审核权限
    private Boolean approvalFlag;

    private Boolean enabledFlag;

    private LocalDate date;

    private LocalDate payStartDate;

    private LocalDate payEndDate;

    private String childName;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String remarks;

    private String relativesDiedRelation;

    private Long deptId;

    private String email;

    private String spouseName;

    private Boolean leaveFlag;
}
