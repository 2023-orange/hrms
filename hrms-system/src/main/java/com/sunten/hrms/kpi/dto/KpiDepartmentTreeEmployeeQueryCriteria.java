package com.sunten.hrms.kpi.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author zhoujy
 * @since 2023-11-27
 */
@Data
public class KpiDepartmentTreeEmployeeQueryCriteria implements Serializable {

    Integer employeeId;

    String year;


}
