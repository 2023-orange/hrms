package com.sunten.hrms.pm.dto;


import com.sunten.hrms.base.BaseDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.util.Set;

/**
 * @author batan
 * @since 2020-08-04
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmEmployeeTempDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 员工id
//    private Long employeeId;
    private PmEmployeeDTO employeeDTO;

    private PmEmployeeTitleDTO employeeTitle;

    // 身高
    private Double height;

    // 体重
    private Double weight;

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

    // 操作标记
    private String instructionsFlag;

    // 校核标记
    private String checkFlag;

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

    private Long id;
    //修改标记
    private Boolean changeFlag;
    //学历
    Set<PmEmployeeEducationDTO> educations;
    //职业资格
    Set<PmEmployeeVocationalDTO> vocationals;
    //家庭情况
    Set<PmEmployeeFamilyDTO> familys;
    // 职称级别
    Set<PmEmployeeTitleDTO> titles;
    // 政治面貌
    Set<PmEmployeePoliticalDTO> politicals;
    // 工作经历
    Set<PmEmployeeWorkhistoryDTO> workhistorys;
    // 工作外职务
    Set<PmEmployeePostotherDTO> postothers;
    // 社会关系
    Set<PmEmployeeSocialrelationsDTO> socialrelations;
    // 技术爱好
    Set<PmEmployeeHobbyDTO> hobbys;
    // 合同情况
    Set<PmEmployeeContractDTO> contracts;
}
