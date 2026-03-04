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
 * 入职情况表
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeEntry extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     */
//    @NotNull
//    private Long employeeId;
    @TableField(exist = false)
    private PmEmployee employee;

    /**
     * 入职时间
     */
    @NotNull
    private LocalDate entryTime;

    /**
     * 入职录用方式
     */
    private String entryMode;

    /**
     * 是否有人事档案
     */
    @NotNull
    private Boolean entryArchivesFlag;

    /**
     * 档案不详
     */
    private String archivesUnknown;

    /**
     * 是否有犯罪记录
     */
    private Boolean crimeFlag;

    /**
     * 试用期（月）
     */
//    @NotNull
    private Integer probation;

    /**
     * 转正时间
     */
    private LocalDate formalTime;

    /**
     * 介绍信工资
     */
    private String introductionWages;

    /**
     * 介绍信情况
     */
    private String introductionSituation;

    /**
     * 档案所在地
     */
    private String archivesAddress;

    /**
     * 过往工龄
     */
//    @NotNull
    private Integer workedYears;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 弹性域1
     */
    private String attribute1;

    /**
     * 弹性域2
     */
    private String attribute2;

    /**
     * 弹性域3
     */
    private String attribute3;

    /**
     * 有效标记默认值
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private LocalDate probationEndTime;

    private String assessFlag; // 是否生成过试用考核，Y是，N否
}
