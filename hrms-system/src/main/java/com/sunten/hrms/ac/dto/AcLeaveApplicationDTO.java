package com.sunten.hrms.ac.dto;

    import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.time.LocalDateTime;

/**
 * @author zouyp
 * @since 2023-05-29
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AcLeaveApplicationDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Integer id;

    // OA单号
    private String oaOrder;

    // 姓名
    private String nickName;

    // 工号
    private String userName;

    // 部门
    private String userDepartment;

    // 科室
    private String userSection;

    // 班组
    private String groups;

    // 岗位
    private String position;

    // 原因
    private String reason;

    // 总休息天数
    private Float totalNumber;

    // 人资修改天数
    private Float hrTotal;

    // 人资修改日期
    private String modifyReason;

    // 版本
    private Integer version;

    // 需重新上传附件标记
    private Integer to_be_uploaded_flag;

    // 当前审批节点
    private String approvalNode;

    // 审批人
    private String approvalEmployee;

    // 最终审批结果
    private String approvalResult;

    // 有效标记
    private Boolean enabledFlag;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private Long attribute2;

    // 弹性域3
    private String attribute3;

    // 弹性域4
    private String attribute4;

    // 弹性域5
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
    private float workNumber;
}
