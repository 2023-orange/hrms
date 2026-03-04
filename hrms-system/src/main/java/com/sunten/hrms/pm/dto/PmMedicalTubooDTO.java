package com.sunten.hrms.pm.dto;

    import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.time.LocalDate;

/**
 * @author zhoujy
 * @since 2022-11-30
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmMedicalTubooDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    // 人事档案ID
    private Long employeeId;

    // 职业禁忌
    private String jobTuboo;

    private String remarks;

    private PmEmployeeDTO pmEmployee;

    private String medicalType;

    private LocalDate medicalTime;

}
