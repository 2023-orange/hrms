package com.sunten.hrms.tool.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author batan
 * @since 2020-10-30
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ToolEmailInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 收件人
    private String sendTo;

    // 主题
    private String mailSubject;

    // 计划发送时间
    private LocalDateTime plannedDate;

    // 实际发送时间
    private LocalDateTime sendDate;

    // 状态
    private String status;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 弹性域4
    private String attribute4;

    // 弹性域5
    private String attribute5;

    //    private Long serverId;
    private ToolEmailServerDTO emailServer;

    private String mailContent;

    private String errorMessage;

    private Long id;


}
