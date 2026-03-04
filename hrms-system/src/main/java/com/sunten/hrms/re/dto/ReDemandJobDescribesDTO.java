package com.sunten.hrms.re.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import lombok.Builder;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-04-23
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ReDemandJobDescribesDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 用人需求岗位子表id
    private Long demandJobId;

    // 职位名称
    private String jobName;

    // 所属部门
    private String deptName;

    // 直接上级
    private String directlyUnder;

    // 职位概要
    private String jobDescribes;

    // 工作内容
    private String jobContent;

    // 任职资格
    private String workQualification;

    // 确认标记（true，信息不能修改）
    private Boolean checkFlag;

    // 生效标记
    private Boolean enabledFlag;

    private Long id;

    private Boolean showBox = false;

    private Long deptId;

    // 学历
    private String education;

    // 性别
    private String sex;

    // 年龄范围
    private String age;

    // 工作年限
    private String workAge;

    // 工作经验年限
    private String workExperenceAge;

    // 必备证件
    private String necessaryCertificates;

    // 其它技能及要求
    private String otherSkillsRequirements;

    // 专业要求
    private String skillsRequirements;

}
