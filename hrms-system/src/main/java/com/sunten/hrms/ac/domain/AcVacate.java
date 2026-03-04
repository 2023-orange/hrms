package com.sunten.hrms.ac.domain;

import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @atuthor xukai
 * @date 2020/10/15 11:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcVacate extends BaseEntity{
    private Long id;

    private String nickName; // 名称

    private String userName; // 工牌号

    private String requisitionCode; // 单号

    private String state; // 状态

    private LocalDateTime startTime; // 假条开始日期

    private LocalDateTime endTime; // 假条结束日期

    private String reason; // 请假原因

    private String leaveType; // 假条类型

    private String checkResult; // 匹配结果

    private String dateStr; // 请假日期集合
}
