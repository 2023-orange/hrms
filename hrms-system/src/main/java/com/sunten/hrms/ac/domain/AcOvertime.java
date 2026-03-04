package com.sunten.hrms.ac.domain;

import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @atuthor xukai
 * @date 2020/10/16 15:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcOvertime extends BaseEntity {
    private Long id;
    private String workCode;// 工牌
    private String nickName; // 姓名
    private String deptName; // 部门
    private String officeName; // 科室
    private String nowDate; // 当前月份
    private Double quantity; // 调休天数
    private Double restNum; // 休息日加班
    private Double workNum; // 工作日加班
}
