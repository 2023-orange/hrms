package com.sunten.hrms.swm.dto;

import com.sunten.hrms.swm.vo.DeptAndAdministrativeOfficeVo;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * @author liangjw
 * @since 2021-02-24
 */
@Data
public class SwmEmpDeptQueryCriteria implements Serializable {
    private String nameOrWorkcard;

    private Boolean enabledFlag;

    private String workCard;

    private String type;

    private Long seId;



}
