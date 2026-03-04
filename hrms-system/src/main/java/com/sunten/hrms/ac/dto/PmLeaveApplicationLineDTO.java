package com.sunten.hrms.ac.dto;

    import java.time.LocalDateTime;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zouyp
 * @since 2023-06-12
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmLeaveApplicationLineDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Integer id;

    // OA申请单号
    private String oaOrder;

    // 请假类别
    private String leaveType;

    // 开始时间
    private LocalDateTime startTime;

    // 结束时间
    private LocalDateTime endTime;

    // 请假天数
    private Float number;

    // 版本号
    private Integer version;

    // 请假的工作天数
    private Float workNumber;

    // 人资修改姓名
    private String hrNickName;

    // 人资修改日期
    private String hrTime;

    // 有效标记
    private Boolean enabledFlag;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private Long attribute2;

    // 弹性域3
    private String attribute3;

    // 弹性域4
    private String attribute4;

    // 弹性域5
    private String attribute5;


}
