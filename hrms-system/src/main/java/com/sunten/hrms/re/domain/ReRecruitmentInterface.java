package com.sunten.hrms.re.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 招骋数据临时表
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class ReRecruitmentInterface extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位名称
     */
//    @NotBlank
//    private String jobName;
    @TableField(exist = false)
    private ReRecruitmentInterface recruitmentInterface;

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
     * 户口性质
     */
    @NotBlank
    private String householdCharacter;

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
     * 户口地址
     */
    @NotBlank
    private String householdAddress;

    /**
     * 户口邮编
     */
    private String householdZipcode;

    /**
     * 是否集体户口
     */
    @NotNull
    private Boolean collectiveHouseholdFlag;

    /**
     * 集体户口所在地
     */
    private String collectiveAddress;

    /**
     * 籍贯
     */
    @NotBlank
    private String nativePlace;

    /**
     * 是否解除原劳动合同
     */
    private Boolean terminateContractFlag;

    /**
     * 是否有职业病
     */
    private Boolean occupationalDiseasesFlag;

    /**
     * 是否解除原单位协议限制
     */
    private Boolean terminationAgreementFlag;

    /**
     * 健康情况
     */
    private Boolean healthFlag;

    /**
     * 入职时间
     */
    @NotNull
    private LocalDate entryTime;

    /**
     * 是否录用
     */
    @NotNull
    private Boolean recruitmentFlag;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 姓名
     */
    @NotBlank
    private String name;

    /**
     * 关系
     */
    @NotBlank
    private String relationship;

    /**
     * 单位
     */
    @NotBlank
    private String company;

    /**
     * 职务
     */
    private String post;

    /**
     * 电话
     */
    private String tele;

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
     * 有效标记默认值
     */
    @NotNull
    private Boolean enabledFlag;

    /**
     * 是否在厂工作
     */
    @NotNull
    private Boolean inFactoryFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


    @TableField(exist = false)
    private Set<ReAwardInterface> awardInterfaces;

    @TableField(exist = false)
    private Set<ReEducationInterface> educationInterfaces;

    @TableField(exist = false)
    private Set<ReFamilyInterface> familyInterfaces;

    @TableField(exist = false)
    private Set<ReHobbyInterface> hobbyInterfaces;

    @TableField(exist = false)
    private Set<ReTitleInterface> titleInterfaces;

    @TableField(exist = false)
    private Set<ReTrainInterface> trainInterfaces;

    @TableField(exist = false)
    private Set<ReWorkhistoryInterface> workhistoryInterfaces;

    private Long reId;
}
