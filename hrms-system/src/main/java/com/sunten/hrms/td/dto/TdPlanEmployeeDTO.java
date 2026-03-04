package com.sunten.hrms.td.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-05-25
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdPlanEmployeeDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 培训计划实施id
    private Long planImplementId;

    // 人员id
    private Long employeeId;

    // 有效标记
    private Boolean enabledFlag;

    // employee、teacher
    private String type;

    private Long id;

    private String employeeName;

    private String email;

}
