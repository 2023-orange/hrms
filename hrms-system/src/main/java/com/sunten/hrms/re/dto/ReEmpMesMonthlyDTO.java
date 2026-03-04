package com.sunten.hrms.re.dto;

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
 * @since 2022-01-07
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ReEmpMesMonthlyDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 年份
    private Integer year;

    // 月份
    private Integer month;

    // 所属日期
    private LocalDate date;

    // 人员id
    private Long employeeId;

    // 岗位id
    private Long jobId;

    // 部门id
    private Long deptId;

    private Long id;


}
