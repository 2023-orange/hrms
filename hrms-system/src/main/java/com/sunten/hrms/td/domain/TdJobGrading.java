package com.sunten.hrms.td.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 岗位分级表
 * </p>
 *
 * @author xukai
 * @since 2022-03-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdJobGrading extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 工序
     */
    private String process;

    /**
     * 认证岗位
     */
    private String certificationJob;

    /**
     * 人事岗位
     */
    private String jobName;

    /**
     * 岗位id(暂时不与岗位挂钩)
     */
    private Long jobId;

    /**
     * 岗位级别
     */
    private String jobLevel;

    /**
     * 工作内容
     */
    private String workContent;

    /**
     * 有效标记
     */
    private Boolean enabledFlag;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 车间名称
     */
    private String teamName;

    @TableId(value = "id", type = IdType.NONE)
    @NotNull
    private Long id;


}
