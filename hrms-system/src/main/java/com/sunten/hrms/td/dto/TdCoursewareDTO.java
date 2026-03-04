package com.sunten.hrms.td.dto;

    import com.sunten.hrms.base.BaseDTO;
import com.sunten.hrms.tool.dto.ToolLocalStorageDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.time.LocalDateTime;
    import java.util.List;

/**
 * @author xukai
 * @since 2021-06-18
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdCoursewareDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 课件名称
    private String name;

    // 归属课程或项目类型
    private String dependentProject;

    // 归属课程或项目id
    private Long dependentId;

    // 课件开发人员类别（内部、外部）
    private String employeeType;

    // 内部人员工号
    private String inWorkCard;

    // 内部人员姓名
    private String inName;

    // 内部员工id
    private Long inEmployeeId;

    // 外部人员姓名
    private String outName;

    // 外部人员机构
    private String outOrganization;

    // 课件适用对象
    private String useObject;

    // 课件权限设置
    private String jurisdictionSetting;

    // 附件id
//    private Long fileId;
    private ToolLocalStorageDTO storage;

    // 可下载标识
    private Boolean downloadFlag;

    // 有效标记
    private Boolean enabledFlag;

    private Long id;

    private List<TdCoursewareEmployeeDTO> employees; // 仅限选中员工子表

    private String approvalStatus; // 审批状态，wait审批中，pass审批通过，notpass驳回

    private String oaOrder; // OA审批单号

    private String lastVerdict; // 最后审批意见

    private LocalDateTime approvalEndTime; // 审批通过时间

    private Boolean selfFlag;

}
