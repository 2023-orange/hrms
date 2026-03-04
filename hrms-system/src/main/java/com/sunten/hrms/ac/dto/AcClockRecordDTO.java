package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import java.time.LocalTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.pm.dto.PmEmployeeDTO;
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
public class AcClockRecordDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 主键非自增，与kq_crjsj的数据的ID一致
    private Long id;

    // 员工id
    private Long employeeId;

    private PmEmployeeDTO employee;

    // 日期
    private LocalDate date;

    // 打卡时间
    private LocalTime clockTime;

    // 门禁控制器编号
    private String cardMachineNo;

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


}
