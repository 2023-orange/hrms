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
 * @author batan
 * @since 2020-08-04
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmEmployeeTitleTempDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    //员工id
    private Long employeeId;
    // 职称id
//    private Long titleId;
    private PmEmployeeTitleDTO employeeTitle;

    // 职称级别
    private String titleLevel;

    // 职称名称
    private String titleName;

    // 评定时间
    private LocalDate evaluationTime;

    // 是否最高职称
    private Boolean newTitleFlag;

    // 操作标记
    private String instructionsFlag;

    // 校核标记
    private String checkFlag;

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
