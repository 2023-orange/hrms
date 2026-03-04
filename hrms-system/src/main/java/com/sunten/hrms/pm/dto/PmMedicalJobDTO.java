package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.util.List;

/**
 * @author xukai
 * @since 2021-04-19
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmMedicalJobDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 部门名称
    private String deptName;

    // 科室名称
    private String officeName;

    // 班组名称
    private String teamName;

    // 对应部门科室班组id
    private Long deptId;

    // 岗位名称
    private String jobName;

    // 岗位id
    private Long jobId;

    // 岗位类别
    private String jobClass;
    // 危害因素
    private String harmFactory;

    // 职业禁忌症
    private String jobTuboo;

    private Long id;

    /**
     * 上岗体检项目字符串
     */
    private String goWorkStr;
    /**
     * 在岗体检项目字符串
     */
    private String inWorkStr;
    /**
     * 离岗体检项目字符串
     */
    private String outWorkStr;
    /**
     * 上岗体检项目子集
     */
    private List<PmMedicalRelevanceDTO> goMedicalRelevanceList;
    /**
     * 在岗体检项目子集
     */
    private List<PmMedicalRelevanceDTO> inMedicalRelevanceList;
    /**
     * 离岗体检项目子集
     */
    private List<PmMedicalRelevanceDTO> outMedicalRelevanceList;

}
