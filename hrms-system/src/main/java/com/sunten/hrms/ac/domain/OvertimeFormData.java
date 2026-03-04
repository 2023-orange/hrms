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
public class OvertimeFormData {
    private String oa_order;
    private String nick_name;
    private String user_name;
    private String department;
    private String administrative_office;
    private String groups;
    private String job_name;
    private String reason;
    private Integer total_number;
    private Float total_time;
    private String approval_result;
}
