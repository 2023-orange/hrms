package com.sunten.hrms.re.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 培训记录表
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class ReTrain extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 招骋员工ID
     */
    private Long reId;
    @TableField(exist = false)
    private ReRecruitment recruitment;

    /**
     * 培训名称
     */
    @NotBlank
    private String trainName;

    /**
     * 开始时间
     */
    @NotNull
    private LocalDate startTime;

    /**
     * 结束时间
     */
    @NotNull
    private LocalDate endTime;

    /**
     * 培训内容
     */
    @NotBlank
    private String trainContent;

    /**
     * 培训类型
     */
    @NotBlank
    private String trainType;

    /**
     * 课时
     */
    @NotNull
    private String trainTime;

    /**
     * 培训地点
     */
    private String trainAddress;

    /**
     * 培训单位
     */
    private String trainCompany;

    /**
     * 所获证书
     */
    private String certificate;

    /**
     * 讲师
     */
    @NotBlank
    private String lecturer;

    /**
     * 讲师信息
     */
    private String lecturerInformation;

    /**
     * 备注
     */
    private String remarks;

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
     * 有效标记默认值
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private String trainTimeNew;
}
