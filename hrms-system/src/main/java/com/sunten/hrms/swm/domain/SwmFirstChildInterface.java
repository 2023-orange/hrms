package com.sunten.hrms.swm.domain;

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
 * 第一胎子女信息登记表
 * </p>
 *
 * @author liangjw
 * @since 2021-08-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmFirstChildInterface extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 工牌号
     */
    @NotBlank
    private String workCard;

    /**
     * 数据分组id
     */
    @NotNull
    private Long groupId;

    /**
     * 员工id
     */
    private Long employeeId;

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
     * 弹性域
     */
    private String attribute1;

    /**
     * 弹性域
     */
    private String attribute2;

    /**
     * 弹性域
     */
    private String attribute3;

    /**
     * 出生日期
     */
    @NotNull
    private LocalDate date;

    /**
     * 子女名称
     */
    private String childName;

    /**
     * 员工姓名
     */
    private String name;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


}
