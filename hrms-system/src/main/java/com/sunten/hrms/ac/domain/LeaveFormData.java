package com.sunten.hrms.ac.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author zouyp
 */
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class LeaveFormData{
    private static final long serialVersionUID = 111L;
    /**
     * 申请单号
     */
    private String requisition_code;

    /**
     * 员工姓名
     */
    private String employee_name;

    /**
     * 工牌号
     */
    private String work_card;

    /**
     * 部门
     */
    private String department;

    /**
     * 科室
     */
    private String administrative_office;

    /**
     * 班组
     */
    private String groups;
    /**
     * 岗位
     */
    private String job_name;

    /**
     * 申请理由
     */
    private String reason;

    /**
     * 请假总天数
     */
    private String total_number;

    /**
     * 工作日天数
     */
    private String work_number;

    /**
     * 上传附件标识
     */
    private Integer ToBeUploadedFlag;
}
