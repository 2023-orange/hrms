package com.sunten.hrms.td.dto;

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
 * @since 2021-06-30
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdCredentialDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 人员id
    private Long employeeId;

    // 证书名称
    private String credentialName;

    // 证书类别
    private String credentialType;

    // 具体项目
    private String specificProject;

    // 发证机构
    private String grantOrganization;

    // 发证日期
    private LocalDate grantDate;

    // 证书有效期
    private LocalDate validityDate;

    // 证书管理处
    private String adminAdress;

    // 证书存放处
    private String storeAdress;

    // 审核标记
    private Boolean appraisalFlag;

    // 有效标记
    private Boolean enabledFlag;

    private Long id;


    private String name;

    private String workCard;

    private String deptName;

    private String department;

    private String team;

    private Boolean checkDialogFlag;

    private Boolean delDialogFlag;

    private String adminAdressType;

    private String storeAdressType;


}
