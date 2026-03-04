package com.sunten.hrms.tool.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author batan
 * @since 2020-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class ToolEmailInterface extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 收件人
     */
    @NotBlank
    private String sendTo;

    /**
     * 主题
     */
    @NotBlank
    private String mailSubject;

    /**
     * 计划发送时间
     */
    @NotNull
    private LocalDateTime plannedDate;

    /**
     * 实际发送时间
     */
    private LocalDateTime sendDate;

    /**
     * 状态
     */
    @NotBlank
    private String status;

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

//    @NotNull
//    private Long serverId;

    @TableField(exist = false)
    private ToolEmailServer emailServer;

    @NotBlank
    private String mailContent;

    private String errorMessage;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


    @TableField(exist = false)
    private String oldStatus;
}
