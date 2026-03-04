package com.sunten.hrms.re.dto;

import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;


/**
 * @author zhoujy
 * @since 2022-11-22
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ReDemandAgreeNumDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    // 需求id
    private Long demandId;

    // 同意用人数
    private Integer agreeNum;

    // 修改人姓名
    private String editBy;

    //岗位
    private String jobName;

    private Integer jobSecondCount;
    private Integer jobThirdCount;
    private Integer jobCurrentCount;

}
