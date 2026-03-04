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
public class SwmBonusDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 主键
    private Long id;

    // 奖金名称
    private String bonusName;

    // 所属月份（格式：年.月）
    private String month;

    // 发放日期
    private String releaseTime;

    // 金额
    private BigDecimal money;

    // 备注
    private String comment;

    // 最后修改人String
    private String lastUpdateBy;

    private Boolean enabledFlag;


}
