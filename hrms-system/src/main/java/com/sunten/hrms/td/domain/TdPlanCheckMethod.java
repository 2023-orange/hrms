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
 * 培训效果评价方式表
 * </p>
 *
 * @author liangjw
 * @since 2022-03-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdPlanCheckMethod extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * td_plan_implement的id
     */
    @NotNull
    private Long planImplementId;

    /**
     * 考核方式
     */
    @NotBlank
    private String checkMethod;

    /**
     * 评价结果
     */
    private String evaluationResults;

    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


}
