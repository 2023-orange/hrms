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
 * @since 2021-08-05
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmConsolationMoneyInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 数据分组id
    private Long groupId;

    // 员工id
    private Long employeeId;

    // 工牌号
    private String workCard;

    // 员工姓名
    private String employeeName;

    // 操作码
    private String operationCode;

    // 错误信息
    private String errorMsg;

    // 数据状态
    private String dataStatus;

    // 慰问金类别
    private String consolationMoneyType;

    // 申请日期
    private LocalDate applicationDate;

    // 申请金额
    private BigDecimal applicationMoney;

    // 逝世亲属及称谓(类别为丧事时填写)
    private String relativesDied;

    // 实际发放金额
    private BigDecimal releasedMoney;

    // 发放标记
    private Boolean releasedFlag;

    // 发放日期
    private LocalDate releasedTime;

    // 有效标记
    private Boolean enabledFlag;

    private Long id;

    private LocalDate date;


    private String childName;

    private String remarks;


}
