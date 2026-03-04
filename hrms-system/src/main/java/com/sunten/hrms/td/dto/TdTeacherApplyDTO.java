package com.sunten.hrms.td.dto;

    import java.math.BigDecimal;
    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.pm.dto.PmEmployeeDTO;
    import com.sunten.hrms.tool.domain.ToolLocalStorage;
    import com.sunten.hrms.tool.dto.ToolLocalStorageDTO;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xukai
 * @since 2021-06-15
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdTeacherApplyDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 讲师
    private PmEmployeeDTO pmEmployee;

    // 课程主题
    private String title;

    // 课程内容
    private String content;

    // 授课经验（年）: 他司+我司
    private BigDecimal teachExperience;

    // 他司经验年限
    private BigDecimal otherExperienceCompose;

    // 我司经验年限
    private BigDecimal myExperienceCompose;

    // 曾授课课题
    private String everExperience;

    // 擅长领域
    private String beGoodAt;

    // 部门意见
    private String deptOpinion;

    // 部门审核人
    private String deptBy;

    // 评审组意见：符合、不符合
    private String judgeOpinion;

    // 经办人
    private String judgeBy;

    // 提交标识，0为保存，1为提交审批
    private Boolean submitFlag;

    // 讲师等级
    private String level;

    // 讲师分数
    private BigDecimal score;

    // 线下认证附件
//    private Long fileId;
    private ToolLocalStorageDTO storage;

    // 部门审核日期
    private LocalDateTime deptOpinionDate;

    // 评审组审核日期
    private LocalDateTime judgeOpinionDate;

    // 认证结果，0认证不通过，1认证通过
    private Boolean attribute3;

    private Long id;
    // 认证标识，0未认证，1已认证
    private Boolean attribute1;

    private String attribute4;

    private String attribute5;

    private LocalDateTime attribute2;

    // 最终审批结果
    private String approvalResult;

    // OA单号
    private String oaOrder;

    // 审批结束日期
    private LocalDateTime approvalDate;

    // 当前审批节点
    private String approvalNode;

    // 审批人
    private String approvalEmployee;

}
