package com.sunten.hrms.re.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 招骋数据表
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class ReRecruitment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位名称
     */
    @NotBlank
    private String jobName;

    /**
     * 部门科室名称
     */
    @NotBlank
    private String deptName;

    /**
     * 期望年薪
     */
    private Double expectedSalary;

    /**
     * 性别
     */
    @NotBlank
    private String gender;

    /**
     * 姓名
     */
    @NotBlank
    private String name;

    /**
     * 身份证号
     */
    @NotBlank
    private String idNumber;

    /**
     * 出生日期
     */
    @NotNull
    private LocalDate birthday;

    /**
     * 身高
     */
    @NotNull
    private Double height;

    /**
     * 体重
     */
    @NotNull
    private Double weight;

    /**
     * 民族
     */
    @NotBlank
    private String nation;

    /**
     * 婚姻状态
     */
    @NotBlank
    private String maritalStatus;

    /**
     * 政治面貌
     */
    private String political;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 现在住址
     */
    @NotBlank
    private String address;

    /**
     * 现住邮编
     */
    private String zipcode;

    /**
     * 籍贯
     */
    @NotBlank
    private String nativePlace;

    /**
     * 是否解除原劳动合同
     */
    @NotNull
    private Boolean originalContractFlag;

    /**
     * 是否有职业病
     */
    @NotNull
    private Boolean occupationalDiseasesFlag;

    /**
     * 是否解除原单位协议限制
     */
    @NotNull
    private Boolean confidentialityRestrictionsFlag;

    /**
     * 健康情况
     */
    @NotNull
    private Boolean healthFlag;

    /**
     * 是否有亲属在我公司工作
     */
    @NotNull
    private Boolean relationshipFlag;

    /**
     * 姓名
     */
    private String relationshipName;

    /**
     * 与本人关系
     */
    private String relationship;

    /**
     * 亲属所在部科室
     */
    private String relationshipDeptName;

    /**
     * 入职时间
     */
    private LocalDate entryTime;

    /**
     * 最终骋用标识
     */
    @NotNull
    private Boolean recruitmentFlag;

    /**
     * 弹性域1
     */
    private String attribute1;

    /**
     * 弹性域2
     */
    private String attribute2;

    /**
     * 弹性域3
     */
    private String attribute3;

    /**
     * 弹性域4
     */
    private String attribute4;

    /**
     * 弹性域5
     */
    private String attribute5;

    /**
     * 入职员工
     */
    @TableField(exist = false)
    private PmEmployee employee;


    /**
     * 最高学历
     */
    @TableField(exist = false)
    private ReEducation batterEducation;

    /**
     * 有效标记默认值
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    @TableField(exist = false)
    private Set<ReAward> awards;

    @TableField(exist = false)
    private Set<ReEducation> educations;

    @TableField(exist = false)
    private Set<ReFamily> families;

    @TableField(exist = false)
    private Set<ReHobby> hobbies;

    @TableField(exist = false)
    private Set<ReTitle> titles;

    @TableField(exist = false)
    private Set<ReTrain> trains;

    @TableField(exist = false)
    private Set<ReWorkhistory> workhistories;

    @TableField(exist = false)
    private Set<ReVocational> vocationals;

    private Integer age;

    private String emergencyContactName;

    private String emergencyContactPhone;
}
