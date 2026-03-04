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
public class SwmAppraisalRulesDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 考核等级
    private String assessmentGrade;

    // 考核权
    private Integer assessmentRight;

    // 工资权
    private BigDecimal wageRight;

    // 是否生效
    private Boolean workFlag;

    private String addReason;

    private String confirmPerson;


}
