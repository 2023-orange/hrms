package com.sunten.hrms.td.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.pm.dto.PmEmployeeDTO;
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
public class TdTrainSubDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 培训id
//    private Long trainId;
    private TdTrainDTO train;

    // 员工id
//    private Long employeeId;
    private PmEmployeeDTO employee;

    // 签到时间
    private LocalDateTime signTime;

    // 离开时间
    private LocalDateTime leaveTime;

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
