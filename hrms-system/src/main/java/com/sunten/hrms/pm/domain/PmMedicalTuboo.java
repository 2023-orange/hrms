package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 
 * </p>
 *
 * @author zhoujy
 * @since 2022-11-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmMedicalTuboo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 人事档案ID
     */
    @NotNull
    private Long employeeId;

    /**
     * 职业禁忌
     */
    private String jobTuboo;

    private String remarks;

    private PmEmployee pmEmployee;

    /**
     * 体检类别
     */
    private String medicalType;

    /**
     * 体检时间
     */
    private LocalDate medicalTime;


}
