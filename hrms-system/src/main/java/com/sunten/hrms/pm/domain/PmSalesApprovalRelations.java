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

/**
 * <p>
 * 销售审批节点关系表
 * </p>
 *
 * @author liangjw
 * @since 2022-02-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmSalesApprovalRelations extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 部门id
     */
    @NotNull
    private Long deptId;

    /**
     * 部门名称
     */
    @NotBlank
    private String deptName;

    /**
     * 所属营销部门审批人id
     */
    @NotNull
    private Long marketDepartmentEmpId;

    /**
     * 所属营销区域审批人id
     */
    @NotNull
    private Long marketAreaEmpId;

    @NotNull
    private Boolean enabledFlag;


}
