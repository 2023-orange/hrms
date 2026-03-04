package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 体检申请表
 * </p>
 *
 * @author xukai
 * @since 2021-04-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmMedical extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 体检类型: 入职体检、调动体检、离职体检、年度体检
     */
    private String medicalType;

    /**
     * 部门名称
     */
    @NotNull
    private String deptName;

    /**
     * 科室名称
     */
    private String officeName;

    /**
     * 部门科室id
     */
    @NotNull
    private Long deptId;

    /**
     * 申请日期
     */
    private LocalDateTime requestDate;

    /**
     * 部门发起人id
     */
    @NotNull
    private Long approvalBy;

    private PmEmployee employee;

    /**
     * 最终审批结果
     */
    private String approvalResult;

    /**
     * OA单号
     */
    private String oaOrder;

    /**
     * 审批结束日期
     */
    private LocalDateTime approvalDate;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;

    /**
     * 当前审批节点
     */
    private String approvalNode;

    /**
     * 审批人
     */
    private String approvalEmployee;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 提交标识，0为保存，1为提交审批
     */
    @NotNull
    private Boolean submitFlag;

    private List<PmMedicalLine> medicalLines;

    private  Integer isFlag;
}
