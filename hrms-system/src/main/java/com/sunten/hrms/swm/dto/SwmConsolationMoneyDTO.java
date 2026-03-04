package com.sunten.hrms.swm.dto;

    import java.math.BigDecimal;
    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-08-04
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmConsolationMoneyDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 人员id
    private Long employeeId;

    // 慰问金类别
    private String consolationMoneyType;

    // 申请日期
    private LocalDate applicationDate;

    // 申请金额
    private BigDecimal applicationMoney;

    // 逝世亲属及称谓(类别为丧事时填写)
    private String relativesDied;

    // 申请单号
    private String oaOrder;

    // 审批日期
    private LocalDate approvalDate;

    // 申请状态
    private String status;

    // 审批状态
    private String approvalStatus;

    // 当前审批节点
    private String currentNode;

    // 当前审批人
    private String currentPerson;

    // 慰问金导出标记
    private Boolean exportFlag;

    // 实际发放金额
    private BigDecimal releasedMoney;

    // 发放标记
    private Boolean releasedFlag;

    // 发放日期
    private LocalDate releasedTime;

    private Long id;
    // 姓名
    private String name;
    // 工号
    private String workCard;
    // 部门
    private String deptName;
    // 科室
    private String departmentName;
    // 班组
    private String teamName;
    // 标记为1的即有审核权限
    private Boolean approvalFlag;

    private Boolean enabledFlag;

    private LocalDate date;

    private LocalDate payStartDate;

    private LocalDate payEndDate;


    private String childName;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String remarks;

    private String relativesDiedRelation;

    private String spouseName;

    private Boolean leaveFlag;


}
