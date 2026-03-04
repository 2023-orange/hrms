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
public class HrUpdateLeaveInfoSub {
    private Long id;
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
    private Float total_number;

    /**
     * 工作日天数
     */
    private Float work_number;
    /**
     * 人资姓名
     */
    private String hr_nick_name;

    /**
     * 修改日期
     */
    private String modify_time;
    /**
     * 创建者
     */
    private String create_by;

    /**
     * 版本号
     */
    private Integer version;
    /**
     * 创建时间
     */
    private String create_time;
}
