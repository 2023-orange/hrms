package com.sunten.hrms.pm.dto;

    import com.sunten.hrms.base.BaseDTO;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.time.LocalDateTime;
    import java.util.List;

/**
 * @author xukai
 * @since 2021-04-07
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmMedicalDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 体检类型: 入职体检、调动体检、离职体检、年度体检
    private String medicalType;

    // 部门名称
    private String deptName;

    // 科室名称
    private String officeName;

    // 部门科室id
    private Long deptId;

    // 申请日期
    private LocalDateTime requestDate;

    // 部门发起人id
    private PmEmployeeDTO employee;
    private Long approvalBy;
    // 最终审批结果
    private String approvalResult;

    // OA单号
    private String oaOrder;

    // 审批结束日期
    private LocalDateTime approvalDate;

    // 有效标记
    private Boolean enabledFlag;

    // 当前审批节点
    private String approvalNode;

    // 审批人
    private String approvalEmployee;

    private Long id;
    // 提交标识，0为保存，1为提交审批
    private Boolean submitFlag;

    private List<PmMedicalLineDTO> medicalLines;

    private  Integer isFlag;
}
