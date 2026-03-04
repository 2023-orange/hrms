package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.fnd.domain.FndJob;
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
public class PmEmployeeJobTransferDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // group任职ID
    private Long groupId;

    // 员工ID
//    private Long employeeId;
    private PmEmployeeDTO employee;

    // 原岗位ID
//    private Long oldJobId;

    // 原岗位名称
//    private String oldJobName;
    private FndJobDTO oldJob;

    // 原部门科室ID
//    private Long oldDeptId;

    // 原部门科室名称
//    private String oldDeptName;
    private FndDeptDTO oldDept;

    // 现岗位ID
//    private Long newJobId;

    // 现岗位名称
//    private String newJobName;
    private FndJobDTO newJob;

    // 现部门科室ID
//    private Long newDeptId;

    // 现部门科室名称2
//    private String newDeptName;
    private FndDeptDTO newDept;

    // 调动开始时间
    private LocalDate startTime;

    // 调动结束时间
    private LocalDate endTime;

    // 调动原因
    private String transferReason;

    // 调动状态
    private String transferState;

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

    private String transferType;

    private String transferError;

    private String transferForm;

    private String updateByUser;

    private Long id;

    private Integer pmTransferRequestId;

    private String attribute6;

}
