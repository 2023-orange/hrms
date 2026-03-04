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
public class SubOvertimeFormData {
    private String oa_order;
    private Integer dept_id;
    private String job_name;
    private String dept_name;
    private String end_time;
    private Float hours;
    private Float month_hours;
    private String name;
    private String start_time;
    private Float total_rest_time;
    private String work_card;
    private String create_by;
    private String create_time;
}
