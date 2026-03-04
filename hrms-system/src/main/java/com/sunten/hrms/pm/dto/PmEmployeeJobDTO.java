package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.fnd.dto.FndDeptDTO;
    import com.sunten.hrms.fnd.dto.FndJobDTO;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2020-08-04
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmEmployeeJobDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // group任职ID
    private Long groupId;

    // 员工ID
//    private Long employeeId;
    private PmEmployeeDTO employee;

    // 岗位ID
//    private Long jobId;

    // 岗位名称
//    private String jobName;
    private FndJobDTO job;

    // 部门科室ID
//    private Long deptId;

    // 部门科室名称
//    private String deptName;
    private FndDeptDTO dept;

    // 备注
    private String remarks;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 弹性域4
    private String attribute4;

    // 弹性域5
    private String attribute5;

    // 有效标记默认值
    private Boolean enabledFlag;

    // 是否主岗位(0否，1是)
    private Boolean jobMainFlag;

    private Long id;

    private Boolean managerFlag;

    private Boolean supervisorFlag;


}
