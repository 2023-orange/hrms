package com.sunten.hrms.fnd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author xukai
 * @since 2021-01-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndAuthorization extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 授权人
     */
    @NotNull
    private Long authorizationBy;

    private PmEmployee byEmployee;

    /**
     * 被授权人
     */
    @NotNull
    private Long authorizationTo;

    private PmEmployee toEmployee;

    /**
     * 授权类型
     */
    @NotBlank
    private String authorizationType;

    /**
     * 授权起始日期
     */
    @NotBlank
    private LocalDate beginDate;

    /**
     * 授权截止日期
     */
    @NotBlank
    private LocalDate endDate;

    /**
     * 生效标识
     */
    @NotNull
    private Boolean enableFlag;

    @TableId(value = "id", type = IdType.NONE)
    @NotNull
    private Long id;

    private List<Long> depts; // 授权部门集合

    private List<FndAuthorizationDts> fndAuthorizationDts;
}
