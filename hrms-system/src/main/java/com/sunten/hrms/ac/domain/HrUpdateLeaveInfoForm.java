package com.sunten.hrms.ac.domain;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Zouyp
 */
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class HrUpdateLeaveInfoForm {
    private String oaOrder;
    private String hrNickName;
    private Float hrTotal;
    private String modifyReason;
    private Integer version;
    private List<HrUpdateLeaveInfoSub>  hrUpdateLeaveInfoSub;
}
