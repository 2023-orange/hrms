package com.sunten.hrms.td.dto;

    import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xukai
 * @since 2022-03-14
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdCoursewareEmployeeDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 课件id
    private Long coursewareId;

    // 选中人员id
    private Long employeeId;

    // 选中人员姓名
    private String employeeName;

    private Long id;


}
