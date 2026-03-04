package com.sunten.hrms.swm.domain;

import java.math.BigDecimal;
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
 * 慰问金接口表
 * </p>
 *
 * @author liangjw
 * @since 2021-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmConsolationMoneyInterface extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据分组id
     */
    @NotNull
    private Long groupId;

    /**
     * 员工id
     */
    @NotNull
    private Long employeeId;

    /**
     * 工牌号
     */
    @NotBlank
    private String workCard;

    /**
     * 员工姓名
     */
    @NotBlank
    private String employeeName;

    /**
     * 操作码
     */
    @NotBlank
    private String operationCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 数据状态
     */
    @NotBlank
    private String dataStatus;

    /**
     * 慰问金类别
     */
    @NotBlank
    private String consolationMoneyType;

    /**
     * 申请日期
     */
    private LocalDate applicationDate;

    /**
     * 申请金额
     */
    @NotNull
    private BigDecimal applicationMoney;

    /**
     * 逝世亲属及称谓(类别为丧事时填写)
     */
    private String relativesDied;

    /**
     * 实际发放金额
     */
    private BigDecimal releasedMoney;

    /**
     * 发放标记
     */
    @NotNull
    private Boolean releasedFlag;

    /**
     * 发放日期
     */
    private LocalDate releasedTime;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private LocalDate date;

    private String childName;

    private String remarks;


}
