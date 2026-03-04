package com.sunten.hrms.ac.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @atuthor xukai
 * @date 2020/10/15 11:18
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AcVacateDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

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
