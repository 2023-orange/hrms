package com.sunten.hrms.kpi.dto;

    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhoujy
 * @since 2023-12-20
 */
@Getter
@Setter
@ToString(callSuper = true)
public class KpiAssessmentIndicatorsInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private String year;

    private Long serialNumber;

    private Double id;

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

    private String parentDepartment;
}
