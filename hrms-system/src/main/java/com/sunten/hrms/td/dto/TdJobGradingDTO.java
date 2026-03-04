package com.sunten.hrms.td.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xukai
 * @since 2022-03-22
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdJobGradingDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 部门id
    private Long deptId;

    // 工序
    private String process;


    /**
     * 认证岗位
     */
    private String certificationJob;

    // 人事岗位
    private String jobName;

    // 岗位id(暂时不与岗位挂钩)
    private Long jobId;

    // 岗位级别
    private String jobLevel;

    // 工作内容
    private String workContent;

    // 有效标记
    private Boolean enabledFlag;

    // 部门名称
    private String deptName;

    // 车间名称
    private String teamName;

    private Long id;


}
