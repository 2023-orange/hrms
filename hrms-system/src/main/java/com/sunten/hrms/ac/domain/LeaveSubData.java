package com.sunten.hrms.ac.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author zouyp
 */
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class LeaveSubData {
    /**
     * 申请单号
     */
    private String oa_order;
    /**
     * 请假类型
     */
    private String leave_type;

    /**
     * 开始时间
     */
    private String start_time;

    /**
     * 结束时间
     */
    private String end_time;

    /**
     * 总天数
     */
    private String total_number;

    /**
     * 工作日天数
     */
    private String work_number;

    /**
     * 创建者
     */
    private String create_by;

    /**
     * 创建时间
     */
    private String create_time;
}
