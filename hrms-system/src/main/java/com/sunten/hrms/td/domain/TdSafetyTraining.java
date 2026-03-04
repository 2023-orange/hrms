package com.sunten.hrms.td.domain;

import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdSafetyTraining extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    // 培训形式
    private String trainingMethod;
    // 培训内容
    private String trainingContent;
    // 主讲人
    private String teacher;
    // 参训人数
    private Integer employeeQuantity;
    // 人数是否含经理
    private Boolean  safetyTrainingHaveManagerFlag;
    // 培训开始日期
    private LocalDate safetyStartDate;
    // 培训结束日期
    private LocalDate safetyEndDate;
    // 安全培训时数
    private Double safetyTrainingTime;
    private Boolean enabledFlag;

    private List<TdSafetyTrainingDept> tdSafetyTrainingDepts;


}
