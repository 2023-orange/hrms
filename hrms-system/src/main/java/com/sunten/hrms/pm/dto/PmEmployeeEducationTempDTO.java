package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;

    import java.time.LocalDate;
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
public class PmEmployeeEducationTempDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

//    private Long employeeId;

    private PmEmployeeDTO employee;
    // 教育id
//    private Long educationId;
    private PmEmployeeEducationDTO employeeEducation;

    // 毕业学校
    private String school;

    // 学历
    private String education;

    // 入学时间
    private LocalDate enrollmentTime;

    // 毕业时间
    private LocalDate graduationTime;

    // 是否最高学历
    private Boolean newEducationFlag;

    // 专业
    private String specializedSubject;

    // 入学性质
    private String enrollment;

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
