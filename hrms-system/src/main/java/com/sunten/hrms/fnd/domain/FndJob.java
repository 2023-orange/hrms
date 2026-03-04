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

import javax.validation.constraints.NotNull;
import java.util.Set;

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
public class FndJob extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    @NotNull
    private String jobName;

    @NotNull
    private Boolean enabledFlag;

    @NotNull
    private Long jobSequence;

    @NotNull
    private Integer authorizedStrength;

    @NotNull
    private String jobCode;

    @NotNull
    private String jobClass;

    private String jobDescribes;

    @NotNull
    private String dataScope;

    @NotNull
    private Boolean deletedFlag;

    //private Long deptId;
    @TableField(exist = false)
    private FndDept dept;

    @TableField(exist = false)
    private Set<FndDept> dataScopeDepts;

    private PmEmployee employee; // 岗位任职人员

    private Long rowId; // 行id，作为某种情形下的伪主键


    // 部门名称
    private String deptName;

    // 班组名称
    private String teamName;

    // 对应部门科室班组id
    private Long deptId;
}
