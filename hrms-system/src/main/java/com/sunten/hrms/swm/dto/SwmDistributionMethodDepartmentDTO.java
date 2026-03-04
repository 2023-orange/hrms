package com.sunten.hrms.swm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.swm.domain.SwmDistributionMethod;
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
public class SwmDistributionMethodDepartmentDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 分配方式id
    private Long distributionMethodId;

    // 部门
    private String department;

    // 科室
    private String administrativeOffice;


    private SwmDistributionMethod swmDistributionMethod;

    private Boolean checkFlag = false;
}
