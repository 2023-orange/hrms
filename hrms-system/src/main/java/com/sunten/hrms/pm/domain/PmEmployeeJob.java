package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 部门科室岗位关系表
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeJob extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * group任职ID
     */
    @NotNull
    private Long groupId;

    /**
     * 员工ID
     */
//    @NotNull
//    private Long employeeId;
    @TableField(exist = false)
    private PmEmployee employee;

    /**
     * 岗位ID
     */
//    @NotNull
//    private Long jobId;

    /**
     * 岗位名称
     */
//    @NotBlank
//    private String jobName;
    @TableField(exist = false)
    private FndJob job;

    /**
     * 部门科室ID
     */
//    @NotNull
//    private Long deptId;

    /**
     * 部门科室名称
     */
//    @NotBlank
//    private String deptName;
    @TableField(exist = false)
    private FndDept dept;

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

    /**
     * 有效标记默认值
     */
    @NotNull
    private Boolean enabledFlag;

    /**
     * 是否主岗位(0否，1是)
     */
    @NotNull
    private Boolean jobMainFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 岗位体检项
     */
    private PmMedical pmMedical;

    private Boolean managerFlag;

    private Boolean supervisorFlag;

    private Boolean salesFlag;
}
