package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 请假申请信息表
 * </p>
 *
 * @author zouyp
 * @since 2023-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmLeaveApplication extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Integer id;

    /**
     * OA单号
     */
    private String oaOrder;

    /**
     * 姓名
     */
    private String nickName;

    /**
     * 工号
     */
    private String userName;

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
     * 岗位
     */
    private String position;

    /**
     * 原因
     */
    private String reason;

    /**
     * 总休息天数
     */
    private Float totalNumber;

    /**
     * 人资修改天数
     */
    private Float hrTotal;

    /**
     *
     * 人资修改日期
     */
    private String modifyReason;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 需重新上传附件标记
     */
    @TableField("toBeUploadedFlag")
    private Integer toBeUploadedFlag;

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
    private Long attribute2;

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
