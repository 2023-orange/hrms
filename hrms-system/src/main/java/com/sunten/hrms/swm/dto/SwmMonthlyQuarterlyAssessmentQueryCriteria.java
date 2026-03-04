package com.sunten.hrms.swm.dto;

import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.AdvancedCriteriaQuery;
import com.sunten.hrms.fnd.dto.FndDynamicQueryBaseCriteria;
import com.sunten.hrms.swm.vo.DeptAndAdministrativeOfficeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SwmMonthlyQuarterlyAssessmentQueryCriteria extends FndDynamicQueryBaseCriteria {

    // 月度、季度
    private String assessmentType;

    // 所得期间
    private String period;

    // 是否经理、主管
    private Boolean managerFlag;

    // 经理主管的部门
    private String managerDepartment;

    // 经理主管的科室
    private String managerAdministrativeOffice;

    // 用于控制经理主管不能看自己的flag
    private Boolean managerLookSelfFlag;

    private Long selfPmEmpId;


    private List<DeptAndAdministrativeOfficeVo> deptAndAdministrativeOfficeVos;

    private Boolean frozenFlag;

}
