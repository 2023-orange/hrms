package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * <p>
 * 工作经历临时表
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeWorkhistoryTemp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 工作经历表id
     */
//    @NotNull
//    private Long headerId;
    @TableField(exist = false)
    private PmEmployeeWorkhistory employeeWorkhistory;

    /**
     * 员工id
     */
//    private Long employeeId;
    @TableField(exist = false)
    private PmEmployee employee;

    /**
     * 单位
     */
    private String company;

    /**
     * 职务
     */
    private String post;

    /**
     * 开始时间
     */
    private LocalDate startTime;

    /**
     * 结束时间
     */
    private LocalDate endTime;

    /**
     * 联系电话
     */
    private String tele;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 操作标记
     */
    private String instructionsFlag;

    /**
     * 校核标记
     */
    private String checkFlag;

    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


}
