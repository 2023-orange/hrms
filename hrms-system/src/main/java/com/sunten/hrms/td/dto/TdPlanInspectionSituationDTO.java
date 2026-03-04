package com.sunten.hrms.td.dto;

    import java.math.BigDecimal;
    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2022-03-11
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdPlanInspectionSituationDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 实施id
    private Long planImplementId;

    // 考核的总人数
    private Integer peopleAssessedTotal;

    // 一次合格人数
    private Integer oneTimePassTotal;

    // 一次合格率
    private BigDecimal primaryPassRate;

    // 补考人数
    private Integer makeExaminationNumber;

    // 补考合格率
    private BigDecimal makePassRate;

    private Long id;

    private Boolean enabledFlag;

    private String checkMethod;


}
