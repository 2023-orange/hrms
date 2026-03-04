package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2022-02-17
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmSalesApprovalRelationsDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 主键
    private Long id;

    // 部门id
    private Long deptId;

    // 部门名称
    private String deptName;

    // 所属营销部门审批人id
    private Long marketDepartmentEmpId;

    // 所属营销区域审批人id
    private Long marketAreaEmpId;

    private Boolean enabledFlag;


}
