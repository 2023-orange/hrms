package com.sunten.hrms.swm.domain;

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
 * 分配方式部门科室关联表
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmDistributionMethodDepartment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 分配方式id
     */
    @NotNull
    private Long distributionMethodId; // 调用update 时，此处存放新的分配方式id

    /**
     * 部门
     */
    private String department;

    /**
     * 科室
     */
    private String administrativeOffice;

    // 有科室： 部门.科室  无科室： 部门
    private String deptCode;


    private SwmDistributionMethod swmDistributionMethod; // 调用update时， 此处存放旧的分配方式


    private Boolean checkFlag = false;
}
