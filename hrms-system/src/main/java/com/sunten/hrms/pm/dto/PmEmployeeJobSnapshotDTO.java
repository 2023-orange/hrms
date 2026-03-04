package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2020-10-15
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmEmployeeJobSnapshotDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 日期
    private LocalDate date;

    // 员工ID
    private Long employeeId;

    // 岗位ID
    private Long jobId;

    // 部门科室ID
    private Long deptId;

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

    private Long id;


}
