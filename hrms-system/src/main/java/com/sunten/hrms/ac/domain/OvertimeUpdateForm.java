package com.sunten.hrms.ac.domain;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Zouyp
 */
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class OvertimeUpdateForm {
    private String oaOrder;
    private String approvalNode;
    private String approvalEmployee;
}
