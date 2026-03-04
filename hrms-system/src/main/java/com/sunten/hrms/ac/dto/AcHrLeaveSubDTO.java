package com.sunten.hrms.ac.dto;

    import java.math.BigDecimal;
    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zouyp
 * @since 2023-05-30
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AcHrLeaveSubDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 主键id
    private Integer id;

    // 申请单号
    private String requisitionCode;

    // 请假类型
    private String leaveType;

    // 开始日期时间
    private String startTime;

    // 结束日期时间
    private String endTime;

    // 请假天数
    private BigDecimal number;

    // 版本号
    private Integer version;

    // 工作日天数
    private BigDecimal workNumber;

    // hr
    private String hrNickName;

    // hr复核时间
    private LocalDateTime hrTime;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;


}
