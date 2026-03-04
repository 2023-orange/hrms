package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 考勤处理记录历史表
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcAttendanceMachine extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 机器id
     */
    @NotNull
    private Long machineId;

    /**
     * 机器名
     */
    @NotNull
    private String machineName;

    /**
     * 机器IP地址
     */
    @NotNull
    private String machineIpAddress;

    /**
     * 机器设备号
     */
    @NotNull
    private String crjqk;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

}
