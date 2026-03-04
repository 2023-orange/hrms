package com.sunten.hrms.td.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-06-18
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdPlanAgreementDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 员工id
    private Long employeeId;

    // 培训计划id
    private Long planId;

    // 服务年限
    private Float serviceYear;

    // 服务开始时间
    private LocalDate beginDate;

    // 服务结束时间
    private LocalDate endDate;

    // 生效标记
    private Boolean enabledFlag;

    private Long id;

    private String checkMethod;


}
