package com.sunten.hrms.ac.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author zouyp
 * @since 2023-05-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcLeaveApplication extends BaseEntity {
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
    /**
     * 请假类型
     */
    private String leaveType;
    /**
     * 请假开始时间
     */
    private LocalDateTime startTime;
    /**
     * 请假结束时间
     */
    private LocalDateTime endTime;
    /**
     * 请假天数
     */
    private float number;
    /**
     * 工作日天数
     */
    private float workNumber;

//    private static final long serialVersionUID = 1L;
//
//    /**
//     * 主键ID
//     */
//    @NotNull
//    private Integer id;
//
//    /**
//     * 申请单号
//     */
//    private String requisitionCode;
//
//    /**
//     * 员工姓名
//     */
//    private String employeeName;
//
//    /**
//     * 工牌号
//     */
//    private String workCard;
//
//    /**
//     * 部门
//     */
//    private String department;
//
//    private String userSection;
//
//    private String groups;
//
//    /**
//     * 岗位
//     */
//    private String jobName;
//
//    /**
//     * 申请理由
//     */
//    private String reason;
//
//    /**
//     * 总申请天数
//     */
//    private BigDecimal totalNumber;
//
//    private BigDecimal hrTotal;
//
//    /**
//     * 审核校验
//     */
//    private String modifyReason;
//
//    /**
//     * 版本号
//     */
//    private Integer version;
//
//    /**
//     * 删除标志
//     */
//    private Boolean deleteFlag;
//
//    /**
//     * 是否上传附件
//     */
//    private Integer toBeUploadedFlag;
//
//    private String attribute1;
//
//    private String attribute2;
//
//    private String attribute3;
//
//    private String attribute4;


}
