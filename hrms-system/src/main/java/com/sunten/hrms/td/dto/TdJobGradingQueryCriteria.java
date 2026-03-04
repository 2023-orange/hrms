package com.sunten.hrms.td.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xukai
 * @since 2022-03-22
 */
@Data
public class TdJobGradingQueryCriteria implements Serializable {
    private String column; // 列名
    private String symbol; // 比较符
    private String value; // 查询值

    private Long deptId; // 部门id
    private String process; // 工序
    private String jobName; // 岗位名称
    /**
     * 人事岗位名称所对应的ID
     */
    private Long jobId;

    /**
     * 认证岗位名称
     */
    private String certificationJob;

    /**
     * 申请调岗位的人员名称
     */
    private String  name;

    /**
     * 申请调岗位的人员的工号
     */
    private String  workCard;


    /**
     * 是否需要“验证申请人的认证岗位是否过期”
     */
    private boolean validityFlag;


    private List<Long> deptIds; // 部门id集合
}
