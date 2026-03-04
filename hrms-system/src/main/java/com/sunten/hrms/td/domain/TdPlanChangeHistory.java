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
 * 培训计划变更历史
 * </p>
 *
 * @author liangjw
 * @since 2021-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdPlanChangeHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父id
     */
    @NotNull
    private Long parentId;

    /**
     * 变更类别(change、cancel、implement)
     */
    @NotBlank
    private String changeType;

    /**
     * 拟更改时间段
     */
    private String changePlanDate;

    /**
     * 变更原因
     */
    private String changeDescribe;

    /**
     * 申请单号
     */
    @NotBlank
    private String oaOrder;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private Boolean passFlag;

    private String updateStr;


}
