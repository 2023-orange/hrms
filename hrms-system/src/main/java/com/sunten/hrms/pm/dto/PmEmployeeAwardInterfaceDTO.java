package com.sunten.hrms.pm.dto;

    import java.math.BigDecimal;
    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xk
 * @since 2021-09-23
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmEmployeeAwardInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 数据分组id
    private Long groupId;

    // 操作码
    private String operationCode;

    // 错误信息
    private String errorMsg;

    // 数据状态
    private String dataStatus;

    // 员工id
    private Long employeeId;

    // 员工工号
    private String workCard;
    // 员工工牌
    private String employeeName;

    // 奖励：Reward；扣罚：Fine
    private String type;

    // 奖罚名称
    private String awardName;

    // 奖罚处理开始时间
    private LocalDateTime awardStarTime;

    // 奖罚处理结束时间
    private LocalDateTime awardEndTime;

    // 奖罚单位
    private String awardCompany;

    // 奖罚内容
    private String awardContent;

    // 奖罚结果
    private String awardResult;

    // 奖罚金额
    private BigDecimal awardMoney;

    // 是否有备查资料
    private Boolean referenceBackupFlag;

    // 备注
    private String remarks;

    private Long id;


}
