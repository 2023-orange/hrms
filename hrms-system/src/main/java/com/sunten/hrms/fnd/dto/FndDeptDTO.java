package com.sunten.hrms.fnd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author batan
 * @since 2019-12-19
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndDeptDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // ID
    private Long id;

    // 名称
    private String deptName;


    // 上级部门
    private Long parentId;

    @NotNull
    private Boolean enabledFlag;

    private String deptCode;

    private String deptLevel;

    private Integer deptSequence;

    private Boolean deletedFlag;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FndDeptDTO> children;

    public String getLabel() {
        return deptName;
    }

    // 部、部门（根据架构树扩展）
    private Long extDeptId;
    private String extDeptName;

    // 科室（根据架构树扩展）
    private Long extDepartmentId;
    private String extDepartmentName;

    // 班组（根据架构树扩展）
    private Long extTeamId;
    private String extTeamName;

    private Boolean used;
    // 管理岗位id
    private Long adminJobId;
}
