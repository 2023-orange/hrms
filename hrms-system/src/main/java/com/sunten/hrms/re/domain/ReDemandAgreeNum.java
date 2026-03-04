package com.sunten.hrms.re.domain;

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
 * @author zhoujy
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class ReDemandAgreeNum extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long id;

    /**
     * 需求id
     */
    @NotNull
    private Long demandId;

    /**
     * 同意用人数
     */
    @NotNull
    private Integer agreeNum;

    /**
     * 修改人姓名
     */
    @NotBlank
    private String editBy;

    /**
     * 岗位
     */
    @NotBlank
    private String jobName;

    @NotNull
    private Integer jobSecondCount;
    @NotNull
    private Integer jobThirdCount;
    @NotNull
    private Integer jobCurrentCount;

}
