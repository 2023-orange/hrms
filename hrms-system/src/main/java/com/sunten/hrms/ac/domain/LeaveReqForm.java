package com.sunten.hrms.ac.domain;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author zouyp
 */
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class LeaveReqForm {
    private LeaveFormData leaveFormData;
    private List<LeaveSubData> leaveSubData;
    private Integer employee_type;
    private String mangerUser;
    private String chargeUser;
    private String foreManUser;
    private String leaderUser;
    private boolean foreManFlags;
    private boolean mangerFlags;
    private boolean leaderFlags;
    private boolean chargeFlags;
}
