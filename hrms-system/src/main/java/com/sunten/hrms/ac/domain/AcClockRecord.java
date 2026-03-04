package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 打卡记录表
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcClockRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键非自增，与kq_crjsj的数据的ID一致
     */
    @TableId(value = "id", type = IdType.NONE)
    @NotNull
    private Long id;

    /**
     * 员工id
     */
    @NotNull
    private Long employeeId;

    private PmEmployee employee;

    /**
     * 日期
     */
    @NotNull
    private LocalDate date;

    /**
     * 打卡时间
     */
    @NotNull
    private LocalTime clockTime;

    /**
     * 门禁控制器编号
     */
    private String cardMachineNo;

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
     * 弹性域4
     */
    private String attribute4;

    /**
     * 弹性域5
     */
    private String attribute5;


}
