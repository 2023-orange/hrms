package com.sunten.hrms.ac.dto;

import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcOvertimeBackUpDTO extends BaseEntity {
    private Long id;
    private Integer total_number;
    private Float average_overtime_hour;
    private Float system_limit_hour;
    private Integer dept_id;
    private String dept_name;
    private Integer enabled_flag;
    private LocalDateTime backup_time;
}
