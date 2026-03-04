package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.pm.domain.PmEmployeeRehire;
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
public class PmEmployeeLeaveofficeDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 员工id
//    private Long employeeId;
    private PmEmployeeDTO employee;

    //返聘
    private PmEmployeeRehireDTO rehire;

    // 离职时间
    private LocalDate quitTime;

    // 岗位名称
    private String jobName;

    // 部门科室名称
    private String deptName;

    // 是否办妥手续
    private Boolean proceduresFlag;

    // 离职证明签收时间
    private LocalDateTime certificateSignTime;

    //离职类型
    private String leaveType;

    // 备注
    private String remarks;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 有效标记默认值
    private Boolean enabledFlag;

    private Long id;


}
