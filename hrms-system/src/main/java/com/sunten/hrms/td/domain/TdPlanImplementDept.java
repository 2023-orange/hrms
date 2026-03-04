package com.sunten.hrms.td.domain;

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
 * 培训实施参与部门扩展表（用于后期统计使用）
 * </p>
 *
 * @author liangjw
 * @since 2021-06-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdPlanImplementDept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 培训计划实施id
     */
    @NotNull
    private Long planImplementId;

    /**
     * 部门id
     */
    @NotNull
    private Long deptId;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;

    /**
     * 部门
     */
    private String deptName;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


}
