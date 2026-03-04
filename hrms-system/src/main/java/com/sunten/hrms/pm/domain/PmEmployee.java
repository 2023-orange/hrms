package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.re.domain.ReRecruitment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 人事档案表
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployee extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 工牌号
     */
    @NotBlank
    private String workCard;

    /**
     * 姓名
     */
    @NotBlank
    private String name;

    /**
     * 姓名拼音
     */
//    @NotBlank
    private String nameAbbreviation;

    /**
     * 性别
     */
//    @NotBlank
    private String gender;

    /**
     * 身份证号
     */
    @NotBlank
    private String idNumber;

    /**
     * 出生日期
     */
//    @NotNull
    private LocalDate birthday;

    /**
     * 身高
     */
//    @NotNull
    private Double height;

    /**
     * 体重
     */
//    @NotNull
    private Double weight;

    /**
     * 农历公历
     */
    private String calendar;

    /**
     * 民族
     */
//    @NotBlank
    private String nation;

    /**
     * 婚姻状态
     */
//    @NotBlank
    private String maritalStatus;

    /**
     * 户口性质
     */
//    @NotBlank
    private String householdCharacter;

    /**
     * 现在住址
     */
//    @NotBlank
    private String address;

    /**
     * 现住邮编
     */
    private String zipcode;

    /**
     * 户口地址
     */
//    @NotBlank
    private String householdAddress;

    /**
     * 户口邮编
     */
    private String householdZipcode;

    /**
     * 是否集体户口
     */
//    @NotNull
    private Boolean collectiveHouseholdFlag;

    /**
     * 集体户口所在地
     */
    private String collectiveAddress;

    /**
     * 籍贯
     */
//    @NotBlank
    private String nativePlace;

    /**
     * 是否考勤
     */
    @NotNull
    private Boolean workAttendanceFlag;

    /**
     * 是否排班
     */
    @NotNull
    private Boolean workshopAttendanceFlag;

    /**
     * 员工标识
     */
//    @NotNull
    private String employeeType;

    /**
     * 是否离职
     */
    @NotNull
    private Boolean leaveFlag;

    /**
     * 职业类别
     */
//    @NotBlank
    private String occupationCategory;

    /**
     * 职业种类
     */
//    @NotBlank
    private String occupationType;

    /**
     * 内部邮箱
     */
    private String emailInside;

    /**
     * 类别
     */
//    @NotBlank
    private String administrationCategory;

    /**
     * 备注
     */
    private String remarks;

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
     * 个人信息最后修改时间
     */
    private LocalDateTime changeTime;

    /**
     * 最后修改数据类型
     */
    private String changeType;

    private String costCenter;
    /**
     * 户口所在地
     */
//    @NotNull
    private String registeredResidence;
    /**
     * 部门
     */
    @TableField(exist = false)
    private  String deptName;
    /**
     * 科室
     */
    @TableField(exist = false)
    private String department;
    /**
     * 班组
     */
    @TableField(exist = false)
    private String  team;

    /**
     * 主岗位
     */
    @TableField(exist = false)
    private PmEmployeeJob  mainJob;

    /**
     * 年龄
     */
    @TableField(exist = false)
    private Integer  age;
    /**
     * 公司内工龄
     */
    private Long companyAge;

    //社保工龄
    private Long workedYears;
    /**
     * 最高学历信息
     */
    @TableField(exist = false)
    private PmEmployeeEducation betterEducation;

    /**
     * 最高职称
     */
    @TableField(exist = false)
    private PmEmployeeTitle betterTitle;
    /**
     * 最高职业资格
     */
    @TableField(exist = false)
    private PmEmployeeVocational betterVocational;
    /**
     * 入职
     */
    @TableField(exist = false)
    private PmEmployeeEntry employeeEntry;
    /**
     * 离职
     */
    @TableField(exist = false)
    private PmEmployeeLeaveoffice employeeLeaveoffice;
    /**
     * 返聘
     */
    @TableField(exist = false)
    private PmEmployeeRehire employeeRehire;
    /**
     * 照片
     */
    @TableField(exist = false)
    private PmEmployeePhoto employeePhoto;
    /**
     * 最新电话记录
     */
    @TableField(exist = false)
    private PmEmployeeTele employeeTele;
    /**
     * 最新紧急联系电话
     */
    @TableField(exist = false)
    private PmEmployeeEmergency employeeEmergencie;
    /**
     * 办公电话
     */
//    @NotBlank
    private String teleoffice;
    /**
     * 家庭电话
     */
//    @NotBlank
    private String telefamily;

    /**
     * 同一身份证号的其它离职信息(修改过，原存放的是其它在职信息)
     */
    @TableField(exist = false)
    private List<PmEmployee> otherEmployees;

    /**
     * 兼任岗位数量（是否兼任标识，大于0则视为有兼任）
     */
    @TableField(exist = false)
    private Long jobQuantity;

    @TableField(exist = false)
    private Set<PmEmployeeAward> employeeAwards;

    @TableField(exist = false)
    private Set<PmEmployeeContract> employeeContracts;

    @TableField(exist = false)
    private Set<PmEmployeeEducation> employeeEducations;

    @TableField(exist = false)
    private Set<PmEmployeeEmergency> employeeEmergencies;

    @TableField(exist = false)
    private Set<PmEmployeeFamily> employeeFamilies;

    @TableField(exist = false)
    private Set<PmEmployeeHobby> employeeHobbies;

    @TableField(exist = false)
    private Set<PmEmployeeJob> employeeJobs;

    @TableField(exist = false)
    private Set<PmEmployeeJobTransfer> employeeJobTransfers;

    @TableField(exist = false)
    private Set<PmEmployeePolitical> employeePoliticals;

    @TableField(exist = false)
    private Set<PmEmployeePostother> employeePostothers;

    @TableField(exist = false)
    private Set<PmEmployeeSocialrelations> employeeSocialrelations;

    @TableField(exist = false)
    private Set<PmEmployeeTele> employeeTeles;

    @TableField(exist = false)
    private Set<PmEmployeeTitle> employeeTitles;

    @TableField(exist = false)
    private Set<PmEmployeeVocational> employeeVocationals;

    @TableField(exist = false)
    private Set<PmEmployeeWorkhistory> employeeWorkhistories;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    //招聘信息数据
    @TableField(exist = false)
    private ReRecruitment recruitment;

    /**
     * 是否需要校核
     */
    @TableField(exist = false)
    private Boolean  needCheck;

    private String jobName;

    // 用于控制考勤权限范围页面的显示
    private Boolean setCheck;
    // 用于区分考勤权限范围管理的权限类别
    private String dataType;

    private Long photoId;

    private String updateByStr;

    private LocalDate tenYearDate;

    private LocalDate twentyYearDate;

    private BigDecimal paidAnnualLeave;

    private Boolean jobTransferFlag; // 有做过调动为true，否则false

    private Boolean salesFlag; // 是否销售员标记

    private String seCostCenter;

    private String seCostCenterNum;
}
