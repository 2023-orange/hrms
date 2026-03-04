package com.sunten.hrms.re.dto;

    import java.math.BigDecimal;
    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-09-08
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ReRecruitmentTempInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 数据分组id
    private Long groupId;

    // 操作码
    private String operationCode;

    // 错误信息
    private String errorMsg;

    // 数据状态
    private String dataStatus;

    // 部门
    private String deptName;

    // 岗位
    private String jobName;

    // 姓名
    private String name;

    // 性别
    private String gender;

    // 身高（厘米）
    private BigDecimal height;

    // 体重（公斤）
    private BigDecimal weight;

    // 出生日期
    private LocalDate birthday;

    // 籍贯
    private String nativePlace;

    // 民族
    private String nation;

    // 婚姻状况
    private String maritalStatus;

    // 政治面貌
    private String political;

    // 手机号码
    private String mobilePhone;

    // E-mail
    private String email;

    // 现居住地址
    private String address;

    // 身份证号码
    private String idNumber;

    // 是否与原来单位解除劳动合同
    private Boolean originalContractFlag;

    // 是否有职业病史
    private Boolean occupationalDiseasesFlag;

    // 是否与原单位解除保密协议
    private Boolean confidentialityRestrictionsFlag;

    // 是否有影响应聘岗位的健康情况
    private Boolean healthFlag;

    // 如被录用，可到职时间
    private LocalDate entryTime;

    // 配偶姓名
    private String halfName;

    // 配偶工作单位
    private String halfCompany;

    // 配偶职务
    private String halfPost;

    // 配偶电话
    private String halfTele;

    // 父亲姓名
    private String fatherName;

    // 父亲工作单位
    private String fatherCompany;

    // 父亲职务
    private String fatherPost;

    // 父亲电话
    private String fatherTele;

    // 母亲姓名
    private String motherName;

    // 母亲工作单位
    private String motherCompany;

    // 母亲职务
    private String motherPost;

    // 母亲电话
    private String motherTele;

    // 子女1姓名
    private String firstChildName;

    // 子女1性别
    private String firstChildSex;

    // 子女1出生日期
    private LocalDate firstChildBirthday;

    // 子女1工作单位/学习地点
    private String firstChildCompany;

    // 子女2姓名
    private String secondChildName;

    // 子女2性别
    private String secondChildSex;

    // 子女2出生日期
    private LocalDate secondChildBirthday;

    // 子女2工作单位/学习地点
    private String secondChildCompany;

    // 是否有亲属在我公司工作
    private Boolean relationshipFlag;

    // 亲属姓名
    private String relationshipName;

    // 亲属所在部门/科室
    private String relationshipDeptName;

    // 与本人关系
    private String relationship;

    // 学历1
    private String firstEducation;

    // 学历1毕业院校
    private String firstSchool;

    // 学历1专业
    private String firstSpecializedSubject;

    // 学历1开始时间
    private LocalDate firstEnrollmentTime;

    // 学历1结束时间
    private LocalDate firstGraduationTime;

    // 学历1性质
    private String firstEnrollment;

    // 学历2
    private String secondEducation;

    // 学历2毕业院校
    private String secondSchool;

    // 学历2专业
    private String secondSpecializedSubject;

    // 学历2开始时间
    private LocalDate secondEnrollmentTime;

    // 学历2结束时间
    private LocalDate secondGraduationTime;

    // 学历2性质
    private String secondEnrollment;

    // 工作单位1
    private String firstCompany;

    // 工作1开始时间
    private LocalDate firstWorkStartTime;

    // 工作1结束时间
    private LocalDate firstWorkEndTime;

    // 工作1部门
    private String firstWorkDepart;

    // 工作1职务
    private String firstWorkPost;

    // 工作1月薪（税前）
    private BigDecimal firstWorkSalaryOld;

    // 工作1离职原因
    private String firstWorkReasonsLeaving;

    // 工作1证明人
    private String firstWorkWitness;

    // 工作1证明人电话
    private String firstWorkTele;

    // 工作单位2
    private String secondCompany;

    // 工作2开始时间
    private LocalDate secondWorkStartTime;

    // 工作2结束时间
    private LocalDate secondWorkEndTime;

    // 工作2部门
    private String secondWorkDepart;

    // 工作2职务
    private String secondWorkPost;

    // 工作2月薪（税前）
    private BigDecimal secondWorkSalaryOld;

    // 工作2离职原因
    private String secondWorkReasonsLeaving;

    // 工作2证明人
    private String secondWorkWitness;

    // 工作2证明人电话
    private String secondWorkTele;

    // 工作单位3
    private String thirdCompany;

    // 工作3开始时间
    private LocalDate thirdWorkStartTime;

    // 工作3结束时间
    private LocalDate thirdWorkEndTime;

    // 工作3部门
    private String thirdWorkDepart;

    // 工作3职务
    private String thirdWorkPost;

    // 工作3月薪（税前）
    private BigDecimal thirdWorkSalaryOld;

    // 工作3离职原因
    private String thirdWorkReasonsLeaving;

    // 工作3证明人
    private String thirdWorkWitness;

    // 工作3证明人电话
    private String thirdWorkTele;

    // 工作单位4
    private String fourthCompany;

    // 工作4开始时间
    private LocalDate fourthWorkStartTime;

    // 工作4结束时间
    private LocalDate fourthWorkEndTime;

    // 工作4部门
    private String fourthWorkDepart;

    // 工作4职务
    private String fourthWorkPost;

    // 工作4月薪（税前）
    private BigDecimal fourthWorkSalaryOld;

    // 工作4离职原因
    private String fourthWorkReasonsLeaving;

    // 工作4证明人
    private String fourthWorkWitness;

    // 工作4证明人电话
    private String fourthWorkTele;

    // 培训1
    private String firstTrain;

    // 培训1开始时间
    private LocalDate firstTrainStartTime;

    // 培训1结束时间
    private LocalDate firstTrainEndTime;

    // 培训1时长
    private String firstTrainTime;

    // 培训1地点
    private String firstTrainAddress;

    // 培训1培训单位
    private String firstTrainCompany;

    // 培训1所获证书
    private String firstTrainCredential;

    // 培训2
    private String secondTrain;

    // 培训2开始时间
    private LocalDate secondTrainStartTime;

    // 培训2结束时间
    private LocalDate secondTrainEndTime;

    // 培训2时长
    private String secondTrainTime;

    // 培训2地点
    private String secondTrainAddress;

    // 培训2培训单位
    private String secondTrainCompany;

    // 培训2所获证书
    private String secondTrainCredential;

    // 奖处1
    private String firstAward;

    // 奖处1获得时间
    private LocalDate firstAwardTime;

    // 奖处1执行部门
    private String firstAwardDept;

    // 奖处1获得原因
    private String firstAwardDescription;

    // 奖处2
    private String secondAward;

    // 奖处2获得时间
    private LocalDate secondAwardTime;

    // 奖处2执行部门
    private String secondAwardDept;

    // 奖处2获得原因
    private String secondAwardDescription;

    // 职称
    private String titleName;

    // 职称级别
    private String titleLevel;

    // 职称获得时间
    private LocalDate titleEvaluationTime;

    // 英语掌握情况
    private String firstLevelSelf;

    // 英语考级
    private String firstLevelMechanism;

    // 其他语种
    private String secondHobby;

    // 其他语种掌握情况
    private String secondLevelSelf;

    // 其他语种考级
    private String secondLevelMechanism;

    // 电脑水平考级
    private String thirdLevelSelf;

    // 熟练运用软件名
    private String fourthHobby;

    // 驾驶证级别
    private String fifthHobby;

    // 爱好特长
    private String sixthHobby;

    private Long id;

    private String submitQuestionTime;

    private Double expectedSalary;

    private String emergencyContactName;

    private String emergencyContactPhone;

    private String needTime;

    private String source;

    private String sourceDetail;

    private String sourceIp;

    private String firstEnrollmentTimeNew;

    private String secondEnrollmentTimeNew;

    private String firstWorkTimeNew;

    private String secondWorkTimeNew;

    private String thirdWorkTimeNew;

    private String fourthWorkTimeNew;

    private String firstTrainTimeNew;

    private String secondTrainTimeNew;

    private Long reId;

    private Boolean recruitmentFlag;

    private Boolean rrEnabledFlag;
}
