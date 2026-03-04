package com.sunten.hrms.pm.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sunten.hrms.base.BaseDTO;
import com.sunten.hrms.pm.domain.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author batan
 * @since 2020-08-04
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmEmployeeDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 查询类型
    private String employeeLikeType;

    // 员工标识
    private String employeeType;

    // 工牌号
    private String workCard;

    // 姓名
    private String name;

    // 姓名拼音
    private String nameAbbreviation;

    // 性别
    private String gender;

    // 身份证号
    private String idNumber;

    // 出生日期
    private LocalDate birthday;

    // 身高
    private Double height;

    // 体重
    private Double weight;

    // 农历公历
    private String calendar;

    // 民族
    private String nation;

    // 婚姻状态
    private String maritalStatus;

    // 户口性质
    private String householdCharacter;

    // 现在住址
    private String address;

    // 现住邮编
    private String zipcode;

    // 户口地址
    private String householdAddress;

    // 户口邮编
    private String householdZipcode;

    // 是否集体户口
    private Boolean collectiveHouseholdFlag;

    // 集体户口所在地
    private String collectiveAddress;

    // 籍贯
    private String nativePlace;

    // 是否考勤
    private Boolean workAttendanceFlag;

    // 是否排班
    private Boolean workshopAttendanceFlag;

    // 是否返聘
    private Boolean rehireFlag;

    // 是否正式工
    private Boolean employeeFlag;

    // 是否离职
    private Boolean leaveFlag;

    // 职业类别
    private String occupationCategory;

    // 职业种类
    private String occupationType;

    // 内部邮箱
    private String emailInside;

    // 类别
    private String administrationCategory;

    // 备注
    private String remarks;

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

    // 有效标记默认值
    private Boolean enabledFlag;

    //户口所在地
    private String registeredResidence;

    private Long id;
    /**
     * 个人信息最后修改时间
     */
    private LocalDateTime changeTime;

    /**
     * 最后修改数据类型
     */
    private String changeType;
    //部门名称
    private  String deptName;

    //科室名称
    private String department;

    //班组名称
    private String  team;

    //年龄
    private Integer  age;

    //公司内工龄
    private Long companyAge;

    //社保工龄
    private Long workedYears;
    //主岗位
    private PmEmployeeJobDTO mainJob;

    //最高学历信息
    private PmEmployeeEducationDTO betterEducation;

    // 最高职称
    private PmEmployeeTitleDTO betterTitle;

    // 最高职业资格
    private PmEmployeeVocationalDTO betterVocational;

    //入职情况
    private PmEmployeeEntryDTO employeeEntry;

    //离职记录
    private PmEmployeeLeaveofficeDTO employeeLeaveoffice;

    //返聘记录
    private PmEmployeeRehireDTO  employeeRehires;
    //照片
    private PmEmployeePhotoDTO employeePhoto;

    // 最新联系电话
    private PmEmployeeTeleDTO  employeeTele;

    // 办公电话
    private String teleoffice;

    // 家庭电话
    private String telefamily;

    // 最新紧急联系电话
    private PmEmployeeEmergencyDTO  employeeEmergencie;

    //岗位数量（是否兼任标识，大于0则视为有兼任）
    private Long jobQuantity;

    private List<PmEmployeeDTO> otherEmployees;

    private List<PmEmployeeDTO> children;

    private Set<PmEmployeeAwardDTO>  employeeAwards;

    private Set<PmEmployeeContractDTO>  employeeContracts;

    private Set<PmEmployeeEducationDTO>  employeeEducations;

    private Set<PmEmployeeEmergencyDTO>  employeeEmergencies;

    private Set<PmEmployeeFamilyDTO>  employeeFamilies;

    private Set<PmEmployeeHobbyDTO>  employeeHobbies;

    private Set<PmEmployeeJobDTO>  employeeJobs;

    private Set<PmEmployeeJobTransferDTO>  employeeJobTransfers;

    private Set<PmEmployeePoliticalDTO>  employeePoliticals;

    private Set<PmEmployeePostotherDTO>  employeePostothers;

    private Set<PmEmployeeSocialrelationsDTO>  employeeSocialrelations;

    private Set<PmEmployeeTeleDTO>  employeeTeles;

    private Set<PmEmployeeTitleDTO>  employeeTitles;

    private Set<PmEmployeeVocationalDTO>  employeeVocationals;

    private Set<PmEmployeeWorkhistoryDTO>  employeeWorkhistories;

    //是否需要校核
    private Boolean  needCheck;

    private String jobName;

    private Boolean setCheck;

    private String dataType;

    private String updateByStr;

    private LocalDate tenYearDate;

    private LocalDate twentyYearDate;

    private BigDecimal paidAnnualLeave;

    private Boolean jobTransferFlag; // 有做过调动为true，否则false

    private String costCenter;

    private Boolean salesFlag; // 是否销售员标记

    private String seCostCenter;

    private String seCostCenterNum;
}
