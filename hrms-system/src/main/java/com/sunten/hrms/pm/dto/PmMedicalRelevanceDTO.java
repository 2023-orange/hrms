package com.sunten.hrms.pm.dto;

    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xukai
 * @since 2021-04-19
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmMedicalRelevanceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 岗位体检表id
    private Long medicalJobId;

    // 体检项实体
//    private Long projectId;
    private PmMedicalProjectDTO medicalProject;

    // 体检项目类别（上岗体检项目、在岗体检项目、离岗体检项目）
    private String projectType;

    private Long id;


}
