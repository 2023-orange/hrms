package com.sunten.hrms.ac.dto;

import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @atuthor xukai
 * @date 2020/10/16 15:58
 */
@Data
public class AcOvertimeQueryCriteria implements Serializable {
    private Boolean adminFlag;// 管理员标识，控制是查询本人还是查询全部
    private String workCode;
    private String name;
    private List<PmEmployeeDTO> employees; // 权限下的人员
    private List<PmEmployeeDTO> queryEmployees;// 查询条件下的人员
    private Long deptId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<Long> deptIds;
    private Long empId;
}
