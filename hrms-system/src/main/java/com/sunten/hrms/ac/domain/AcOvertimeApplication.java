package com.sunten.hrms.ac.domain;

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
 * @author zouyp
 * @since 2023-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcOvertimeApplication extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Integer id;

    /**
     * OA单号
     */
    private String oaOrder;

    /**
     * 申请人姓名
     */
    private String nickName;

    /**
     * 申请人工号
     */
    private String userName;

    /**
     * 加班类别
     */
    private String overtimeType;

    /**
     * 部门
     */
    private String userDepartment;

    /**
     * 科室
     */
    private String userSection;

    /**
     * 班组
     */
    private String groups;

    /**
     * 原因
     */
    private String reason;

    /**
     * 加班总人数
     */
    private Integer totalNumber;

    /**
     * 加班总时数
     */
    private Float totalTime;

    /**
     * 当前审批节点
     */
    private String approvalNode;

    /**
     * 审批人
     */
    private String approvalEmployee;

    /**
     * 最终审批结果
     */
    private String approvalResult;

    /**
     * 有效标记
     */
    private Boolean enabledFlag;

    /**
     * 弹性域1
     */
    private String attribute1;

    /**
     * 弹性域2
     */
    private String attribute2;

    /**
     * 弹性域3
     */
    private String attribute3;

    /**
     * 弹性域4
     */
    private String attribute4;

    /**
     * 弹性域5
     */
    private String attribute5;


}
