package com.sunten.hrms.td.dto;

    import java.math.BigDecimal;
    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.td.domain.TdPlan;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-05-25
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdPlanImplementDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 培训计划id
    private Long planId;

    // 起始培训时间
    private LocalDate beginDate;

    // 培训结束时间
    private LocalDate endDate;

    // 培训时长
    private Float trainingTimeQuantity;

    // 培训地址
    private String trainingAddress;

    // 考核方式
    private String checkMethod;

    // 培训费用
    private BigDecimal trainingMoney;

    // 申请人的empId
    private Long requestBy;

    // 申请日期
    private LocalDate requestDate;

    // 当前审批节点
    private String currentNode;

    // 当前审批人
    private String currentPerson;

    // OA单号
    private String oaOrder;

    // 有效标记
    private Boolean enabledFlag;

    // 申请状态
    private String approvalStatus;

    // 外部讲师
    private String outTeacher;

    // 外部参训人员
    private String outEmp;

    private Long id;

    private TdPlanDTO plan;

    private String paymentDes; // 培训花费明细

    private String overallAttendanceDescription; // 整体培训情况描述

    private Integer absenceTotal; // 缺席人数

    private Integer practicalParticipationTotal; // 实际参训人数


}
