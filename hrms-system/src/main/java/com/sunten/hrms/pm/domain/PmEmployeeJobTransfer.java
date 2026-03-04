package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.domain.FndJob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 岗位调动表
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeJobTransfer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * group任职ID
     */

    private Long groupId;

    /**
     * 员工ID
     */
//    @NotNull
//    private Long employeeId;
    @TableField(exist = false)
    private PmEmployee employee;
    /**
     * 原岗位ID
     */
//    @NotNull
//    private Long oldJobId;

    /**
     * 原岗位名称
     */
//    @NotBlank
//    private String oldJobName;
    @TableField(exist = false)
    private FndJob oldJob;

    /**
     * 原部门科室ID
     */
//    @NotNull
//    private Long oldDeptId;

    /**
     * 原部门科室名称
     */
//    @NotBlank
//    private String oldDeptName;
    @TableField(exist = false)
    private FndDept oldDept;

    /**
     * 现岗位ID
     */
//    @NotNull
//    private Long newJobId;

    /**
     * 现岗位名称
     */
//    @NotBlank
//    private String newJobName;
    @TableField(exist = false)
    private FndJob newJob;

    /**
     * 现部门科室ID
     */
//    @NotNull
//    private Long newDeptId;

    /**
     * 现部门科室名称2
     */
//    @NotBlank
//    private String newDeptName;
    @TableField(exist = false)
    private FndDept newDept;

    /**
     * 调动开始时间
     */
    @NotNull
    private LocalDate startTime;

    /**
     * 调动结束时间
     */
    private LocalDate endTime;

    /**
     * 调动原因
     */
    private String transferReason;

    /**
     * 调动状态
     */
    private String transferState;

    /**
     * 备注
     */
    private String remarks;

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



    private String transferType;

    private String transferError;

    private String transferForm;

    private String updateByUser;
    /**
     * 有效标记默认值
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private Integer pmTransferRequestId;

    private String attribute6;
}
