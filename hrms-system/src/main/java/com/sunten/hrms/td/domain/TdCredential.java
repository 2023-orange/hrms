package com.sunten.hrms.td.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
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
 * 培训证书表
 * </p>
 *
 * @author liangjw
 * @since 2021-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdCredential extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 人员id
     */
    @NotNull
    private Long employeeId;

    /**
     * 证书名称
     */
    @NotBlank
    private String credentialName;

    /**
     * 证书类别
     */
//    @NotBlank
    private String credentialType;

    /**
     * 具体项目
     */
    private String specificProject;

    /**
     * 发证机构
     */
    @NotBlank
    private String grantOrganization;

    /**
     * 发证日期
     */
    @NotNull
    private LocalDate grantDate;

    /**
     * 证书有效期
     */
    private LocalDate validityDate;

    /**
     * 证书管理处
     */
    @NotBlank
    private String adminAdress;

    /**
     * 证书存放处
     */
    @NotBlank
    private String storeAdress;

    /**
     * 审核标记
     */
    @NotNull
    private Boolean appraisalFlag;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private String name;

    private String workCard;

    private String deptName;

    private String department;

    private String team;

    private Boolean checkDialogFlag;

    private Boolean delDialogFlag;

    /**
     * 证书管理处分类
     */
    @NotBlank
    private String adminAdressType;

    /**
     * 证书存放处分类
     */
    @NotBlank
    private String storeAdressType;


}
