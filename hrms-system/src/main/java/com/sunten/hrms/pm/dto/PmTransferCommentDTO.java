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
 * @author xukai
 * @since 2021-05-24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmTransferCommentDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 调动申请表id
    private Long transferId;

    // 实习通知日期
    private LocalDate internshipDate;

    // 实习记录人
    private String internshipBy;

    // 实习工资实施日期
    private LocalDate internshipSalaryDate;

    // 实习工资实施记录人
    private String internshipSalaryBy;

    // 上岗资格证日期
    private LocalDate jobProveDate;

    // 上岗资格证审核人
    private String jobProveBy;

    // 上岗通知日期
    private LocalDate goJobDate;

    // 上岗通知记录人
    private String goJobBy;

    // 实施调入岗薪资日期
    private LocalDate salaryInformDate;

    // 实施调入薪资记录人
    private String salaryInformBy;

    // 未调动原因
    private String failureReazon;

    // 未调动日期
    private LocalDate failureDate;

    // 未调动记录人
    private String failureBy;

    // 借调通知日期
    private LocalDate shortDate;

    // 借调通知记录人
    private String shortBy;

    // 借调薪资实施日期
    private LocalDate shortSalaryDate;

    // 借调薪资实施记录人
    private String shortSalaryBy;

    private Long id;

    private String attribute2;

    private String attribute3;

    private String attribute1;


}
