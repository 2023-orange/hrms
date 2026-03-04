package com.sunten.hrms.re.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 用人需求招聘过程记录
 * </p>
 *
 * @author liangjw
 * @since 2022-01-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class ReDemandTracking extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 需求id
     */
    @NotNull
    private Long demandId;

    /**
     * 过程记录
     */
    @NotBlank
    private String reContent;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private String editBy;

    private Boolean enabledFlag;


}
