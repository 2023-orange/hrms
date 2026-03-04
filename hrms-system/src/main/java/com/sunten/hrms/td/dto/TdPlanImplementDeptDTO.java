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
 * @since 2021-06-21
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdPlanImplementDeptDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 培训计划实施id
    private Long planImplementId;

    // 部门id
    private Long deptId;

    // 有效标记
    private Boolean enabledFlag;

    // 部门
    private String deptName;

    private Long id;


}
