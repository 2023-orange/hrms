package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.pm.domain.PmMedicalLineRelevance;
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
public class PmMedicalLineDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;
    // 体检申请主表ID
    private Long medicalId;
    private PmMedicalDTO medical;
    //人员信息
    private PmEmployeeDTO pmEmployee;
    // 作业类别及危害因素
    private String workAndHazard;

    // 岗位名称
    private String jobName;

    // 岗位id
    private Long jobId;

    //部门名称
    private  String deptName;

    //科室名称
    private String department;

    // 人员名称
    private String employeeName;
    private String workCard;

    // 数量
    private Integer quantity;

    // 体检类别
    private String medicalClass;

    // 相关id(离职、调动申请的id)
    private Long withId;

    // 体检项目名称
    private String medicalName;

    /**
     * 具体体检项目
     */
    private List<PmMedicalLineRelevanceDTO> medicalLineRelevanceList;

    // 体检结果标记
    private String medicalResult;

    // 体检备注
    private String remake;

    // 有效标记
    private Boolean enabledFlag;

    private Long id;
    // 人员id
    private Long employeeId;


    private LocalDateTime firstTime;

    /**
     * 调到部门
     */
    private  String toDeptName;
    /**
     * 调到科室
     */
    private String toDepartment;
    /**
     * 调到班组
     */
    private String toTeamName;
    /**
     * 调到岗位
     */
    private String toJobName;
    /**
     * 调到岗位ID
     */
    private String toJobId;
    /**
     * 原部门
     */
    private  String fromDeptName;
    /**
     * 原科室
     */
    private String fromDepartment;
    /**
     * 原班组
     */
    private String fromTeamName;

}
