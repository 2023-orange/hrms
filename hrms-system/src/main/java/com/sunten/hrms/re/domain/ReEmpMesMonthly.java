package com.sunten.hrms.re.domain;

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
 * 每月人员情况存档
 * </p>
 *
 * @author liangjw
 * @since 2022-01-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class ReEmpMesMonthly extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 年份
     */
    @NotNull
    private Integer year;

    /**
     * 月份
     */
    @NotNull
    private Integer month;

    /**
     * 所属日期
     */
    @NotNull
    private LocalDate date;

    /**
     * 人员id
     */
    @NotNull
    private Long employeeId;

    /**
     * 岗位id
     */
    @NotNull
    private Long jobId;

    /**
     * 部门id
     */
    @NotNull
    private Long deptId;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


}
