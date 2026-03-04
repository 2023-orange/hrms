package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
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
public class PmEmployeeContractDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 员工id
//    private Long employeeId;
    private PmEmployeeDTO employee;

    // 合同类别
    private String contractType;

    // 合同开始时间
    private LocalDateTime startTime;

    // 合同结束时间
    private LocalDateTime endTime;

    // 合同期限
    private String contractValidity;

    // 是否最新合同
    private Boolean newContractFlag;

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
    // 合同次数
    private Long quantity;

}
