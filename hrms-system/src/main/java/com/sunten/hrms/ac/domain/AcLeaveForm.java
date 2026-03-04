package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcLeaveForm extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 工牌
    private String workCard;

    // 员工id
    private PmEmployee pmEmployee;

    // 请假开始时间
    private String startTime;

    // 请假结束时间
    private String  endTime;


}
