package com.sunten.hrms.pm.dto;

import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * @author batan
 * @since 2020-08-04
 */
@Data
public class PmEmployeeJobQueryCriteria implements Serializable {
    private Long employeeId;
    private List<Long> depts;
    private List<Long> jobs;
    private List<PmEmployee> employees;
    private Boolean jobMainFlag;
    private Boolean enabled;
    private Boolean leaveFlag;
}
