package com.sunten.hrms.fnd.dto;

import com.sunten.hrms.base.BaseDTO;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * @author batan
 * @since 2019-12-19
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class FndJobDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String jobName;

    private Long jobSequence;

    private Integer authorizedStrength;

    private String jobCode;

    private Long deptId;

    private String jobDescribes;

    private String jobClass;

    private Boolean enabledFlag;

    private String dataScope;

    private Set<FndDeptDTO> dataScopeDepts;

    private Boolean deletedFlag;

    private FndDeptDTO dept;

    private String deptSuperiorName;

    private PmEmployeeDTO employee; // 岗位任职人员

    private Long rowId; // 行id，作为某种情形下的伪主键

    public FndJobDTO(String jobName, Boolean enabledFlag) {
        this.jobName = jobName;
        this.enabledFlag = enabledFlag;
    }
}
