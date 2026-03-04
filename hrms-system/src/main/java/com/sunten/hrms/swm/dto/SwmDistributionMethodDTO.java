package com.sunten.hrms.swm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmDistributionMethodDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 分配方式
    private String distributionMethod;

    // 生产区分(1为生产，0非生产)
    private Boolean generationDifferentiationFlag;

    // 有效标记（1有效、0无效）
    private Boolean enabledFlag;


}
