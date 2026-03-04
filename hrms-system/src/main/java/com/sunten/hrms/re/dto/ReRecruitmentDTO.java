package com.sunten.hrms.re.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import java.util.Set;

    import com.baomidou.mybatisplus.annotation.TableField;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.pm.domain.PmEmployee;
    import com.sunten.hrms.re.domain.ReEducation;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2020-08-05
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ReRecruitmentDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 岗位名称
    private String jobName;

    // 部门科室名称
    private String deptName;

    // 期望年薪
    private Double expectedSalary;

    // 性别
    private String gender;

    // 姓名
    private String name;

    // 身份证号
    private String idNumber;

    // 出生日期
    private LocalDate birthday;

    // 身高
    private Double height;

    // 体重
    private Double weight;

    // 民族
    private String nation;

    // 婚姻状态
    private String maritalStatus;

    // 政治面貌
    private String political;

    // 手机号
    private String mobilePhone;

    // 邮箱
    private String email;

    // 现在住址
    private String address;

    // 现住邮编
    private String zipcode;

    // 籍贯
    private String nativePlace;

    // 是否解除原劳动合同
    private Boolean originalContractFlag;

    // 是否有职业病
    private Boolean occupationalDiseasesFlag;

    // 是否解除原单位协议限制
    private Boolean confidentialityRestrictionsFlag;

    // 健康情况
    private Boolean healthFlag;

    // 是否有亲属在我公司工作
    private Boolean relationshipFlag;

    // 姓名
    private String relationshipName;

    // 与本人关系
    private String relationship;

    // 亲属所在部科室
    private String relationshipDeptName;

    // 入职时间
    private LocalDate entryTime;

    // 最终骋用标识
    private Boolean recruitmentFlag;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 弹性域4
    private String attribute4;

    // 弹性域5
    private String attribute5;

    private PmEmployee employee;

    // 有效标记默认值
    private Boolean enabledFlag;

    private Long id;

    //最高学历
    private ReEducationDTO batterEducation;

    private Set<ReAwardDTO> awards;

    private Set<ReEducationDTO> educations;

    private Set<ReFamilyDTO> families;

    private Set<ReHobbyDTO> hobbies;

    private Set<ReTitleDTO> titles;

    private Set<ReTrainDTO> trains;

    private Set<ReWorkhistoryDTO> workhistories;

    private Set<ReVocationalDTO> vocationals;
    private Integer age;

    private String emergencyContactName;

    private String emergencyContactPhone;
}
