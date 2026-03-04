package com.sunten.hrms.swm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.math.BigDecimal;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmWageDistributionDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 所得期间（格式：年.月）
    private String incomePeriod;

    // 分配方式id
    private Long distributionMethodId;

    // 分配方式
    private String distributionMethod;

    // 生成区分（1生成，0非生产）
    private Boolean generationDifferentiationFlag;

    // 生产系数
    private BigDecimal productionFactor;

    // 质量系数
    private BigDecimal qualityFactor;

    // 弹性域
    private BigDecimal attribute5;

    // 弹性域
    private BigDecimal attribute4;

    // 弹性域
    private BigDecimal attribute3;

    // 弹性域
    private BigDecimal attribute1;

    // 弹性域
    private BigDecimal attribute2;


    private Boolean enabledFlag;

    private Boolean checkFlag = false;// 前台用编辑标记

}
