package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import java.util.List;

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
 * OA审批通过的请假条临时表
 * </p>
 *
 * @author liangjw
 * @since 2020-10-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcLeaveFormTemp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 工牌号
     */
    @NotBlank
    private String workCard;

    /**
     * 开始日期时间
     */
    @NotNull
    private LocalDateTime startTime;

    /**
     * 结束日期时间
     */
    @NotNull
    private LocalDateTime endTime;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private PmEmployee pmEmployee;

    private List<AcAttendanceRecordTemp> acAttendanceRecordTempList;
}
