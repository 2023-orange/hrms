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
 * 工资分配（工资系数）表
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmWageDistribution extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 所得期间（格式：年.月）
     */
    @NotBlank
    private String incomePeriod;

    /**
     * 分配方式id
     */
    @NotNull
    private Long distributionMethodId;

    /**
     * 分配方式
     */
    @NotBlank
    private String distributionMethod;

    /**
     * 生成区分（1生成，0非生产）
     */
    @NotNull
    private Boolean generationDifferentiationFlag;

    /**
     * 生产系数
     */
    @NotNull
    private BigDecimal productionFactor;

    /**
     * 质量系数
     */
    @NotNull
    private BigDecimal qualityFactor;

    /**
     * 弹性域
     */
    private BigDecimal attribute5;

    /**
     * 弹性域
     */
    private BigDecimal attribute4;

    /**
     * 弹性域
     */
    private BigDecimal attribute3;

    /**
     * 弹性域
     */
    private BigDecimal attribute1;

    /**
     * 弹性域
     */
    private BigDecimal attribute2;

    private Boolean enabledFlag;

    private Boolean checkFlag = false;// 前台用编辑标记
}
