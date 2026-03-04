package com.sunten.hrms.td.vo;

import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.td.domain.TdPlan;
import com.sunten.hrms.td.domain.TdPlanImplement;
import com.sunten.hrms.td.domain.TdTeacher;
import lombok.Data;

/**
 * @atuthor xukai
 * @date 2021/7/5 15:08
 */
@Data
public class TeachingReportVo extends BaseEntity {
    private TdTeacher teacher;
    private TdPlanImplement planImplement;
    private TdPlan tdPlan;
}
