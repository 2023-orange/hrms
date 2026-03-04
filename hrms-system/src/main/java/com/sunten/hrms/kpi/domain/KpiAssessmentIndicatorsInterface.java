package com.sunten.hrms.kpi.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author zhoujy
 * @since 2023-12-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class KpiAssessmentIndicatorsInterface extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String year;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Double id;

    private Long serialNumber;

    private String assessedDepartmentName;

    private Long assessedDepartmentId;

    private String examineType;

    private String targetDimension;

    private String keyPerformanceIndicator;

    private String define;

    private String weight;

    private String threshold;

    private String targetValue;

    private String challengeValue;

    private Long examineDepartmentId;

    private String examineDepartmentName;

    private String examineEmployeeName;

    private String examineEmployeeWorkCard;

    private Long examineEmployeeId;

    private String dataAccuracy;

    private String dataType;

    private Long groupId;

    private String operationCode;

    private String errorMsg;

    private String dataStatus;

    // KPI考核年度编号
    private Long kpiAnnualId;

    private String parentDepartment;
}
