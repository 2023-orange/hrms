package com.sunten.hrms.pm.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author xukai
 * @since 2021-04-07
 */
@Data
public class PmMedicalLineQueryCriteria implements Serializable {
    private Boolean approvalFlag; // 审批完成标记
    private Long medicalId; // 申请主表id
    private Long deptAllId; // 查部门及以下节点
    private Set<Long> deptIds; // 权限部门
    private String column; // 列名
    private String symbol; // 比较符
    private String value; // 查询值

    private List<Long> ids; // 批量反馈结果的id集合

    private Integer id; // 批量结果
    private String medicalResult; // 批量结果
    private String remake; // 批量备注
    private Long updateBy;// 修改人
    private LocalDateTime firstTime;
    private String batchMedicalResult; // 批量结果
    private String batchRemark; // 批量备注

    private Long employeeId; // 员工ID
}
