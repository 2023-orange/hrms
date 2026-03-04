package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 离职记录表
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeLeaveoffice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     */
//    @NotNull
//    private Long employeeId;
    @TableField(exist = false)
    private PmEmployee employee;

    /**
     * 返聘
     */
    @TableField(exist = false)
    private PmEmployeeRehire rehire;

    /**
     * 离职时间
     */
    @NotNull
    private LocalDate quitTime;

    /**
     * 岗位名称
     */
    @NotBlank
    private String jobName;

    /**
     * 部门科室名称
     */
    @NotBlank
    private String deptName;

    /**
     * 是否办妥手续
     */
    @NotNull
    private Boolean proceduresFlag;

    /**
     * 离职证明签收时间
     */
    @NotNull
    private LocalDateTime certificateSignTime;

    /**
     * 离职类型
     */
    @NotNull
    private String leaveType;

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
     * 有效标记默认值
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


}
