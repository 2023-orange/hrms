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
import java.math.BigDecimal;

/**
 * <p>
 * 奖金表
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmBonus extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 奖金名称
     */
    @NotBlank
    private String bonusName;

    /**
     * 所属月份（格式：年.月）
     */
    @NotBlank
    private String month;

    /**
     * 发放日期
     */
    @NotBlank
    private String releaseTime;

    /**
     * 金额
     */
    @NotNull
    private BigDecimal money;

    /**
     * 备注
     */
    private String comment;

    // 最后修改人String
    private String lastUpdateBy;

    private Long userEmpId;

    private Boolean enabledFlag;


}
