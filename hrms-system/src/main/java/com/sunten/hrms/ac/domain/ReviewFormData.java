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
public class ReviewFormData {
    private String end_time;
    private Float hours;
    private String job_name;
    private Float month_hours;
    private String name;
    private String oa_order;
    private String remarks;
    private String review_endTime;
    private Float review_hours;
    private String review_startTime;
    private String review_time;
    private Float review_total_rest_time;
    private String reviewer_nick_name;
    private String reviewer_user_name;
    private String start_time;
    private Float total_rest_time;
    private String work_card;
}
