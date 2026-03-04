package com.sunten.hrms.swm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 薪酬员工基本信息接口表
 * </p>
 *
 * @author zhoujy
 * @since 2023-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmEmployeeInfoInterface extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Double id;

    private Long employeeId;

    private String workCard;

    private String name;

    /**
     * 数据分组id
     */
    private Long groupId;

    /**
     * 操作码
     */
    private String operationCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 数据状态
     */
    private String dataStatus;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 部门
     */
    private String department;

    /**
     * 科室
     */
    private String administrativeOffice;

    /**
     * 班组
     */
    private String team;

    /**
     * 岗位
     */
    private String station;

    /**
     * 成本中心号
     */
    private String costCenterNum;

    /**
     * 成本中心名称
     */
    private String costCenter;

    /**
     * 服务部门
     */
    private String serviceDepartment;


}
