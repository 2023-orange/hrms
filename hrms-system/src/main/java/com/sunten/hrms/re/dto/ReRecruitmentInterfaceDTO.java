package com.sunten.hrms.re.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
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
public class ReRecruitmentInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 岗位名称
    private String jobName;

    // 部门科室名称
    private String deptName;

    // 期望年薪
    private Double expectedSalary;

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

    // 是否解除原劳动合同
    private Boolean terminateContractFlag;

    // 是否有职业病
    private Boolean occupationalDiseasesFlag;

    // 是否解除原单位协议限制
    private Boolean terminationAgreementFlag;

    // 健康情况
    private Boolean healthFlag;

    // 入职时间
    private LocalDate entryTime;

    // 是否录用
    private Boolean recruitmentFlag;

    // 备注
    private String remarks;

    // 姓名
    private String name;

    // 关系
    private String relationship;

    // 单位
    private String company;

    // 职务
    private String post;

    // 电话
    private String tele;

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

    // 是否在厂工作
    private Boolean inFactoryFlag;

    private Long id;

    private Long reId;
}
