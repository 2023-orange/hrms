package com.sunten.hrms.td.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 培训协议书记录表
 * </p>
 *
 * @author liangjw
 * @since 2021-06-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdPlanAgreement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     */
    @NotNull
    private Long employeeId;

    /**
     * 培训计划id
     */
    @NotNull
    private Long planId;

    /**
     * 服务年限
     */
    @NotNull
    private Float serviceYear;

    /**
     * 服务开始时间
     */
    @NotNull
    private LocalDate beginDate;

    /**
     * 服务结束时间
     */
    @NotNull
    private LocalDate endDate;

    /**
     * 生效标记
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private String source;

    private Long sourceId;

    private String type;

    private MultipartFile[] files;

    private String checkMethod;
}
