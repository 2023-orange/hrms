package com.sunten.hrms.fnd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndDept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 名称
     */
    @NotBlank
    private String deptName;

    private String deptCode;

    /**
     * 上级部门
     */
    @NotNull
    private Long parentId;


    private String deptLevel;

    @NotNull
    private Boolean enabledFlag;

    @NotNull
    private Long deptSequence;

    @NotNull
    private Boolean deletedFlag;

    /**
     * 部、部门（根据架构树扩展）
     */
    private Long extDeptId;
    private String extDeptName;

    /**
     * 科室（根据架构树扩展）
     */
    private Long extDepartmentId;
    private String extDepartmentName;

    /**
     * 班组（根据架构树扩展）
     */
    private Long extTeamId;
    private String extTeamName;
    // 管理岗位id
    private Long adminJobId;
    // 管理人员
    private PmEmployee pmEmployee;
}
