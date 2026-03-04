package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.pm.domain.PmEmployee;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2020-08-04
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmEmployeeAwardDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 员工id
//    private Long employeeId;
    private PmEmployeeDTO employee;

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
    private Double awardMoney;

    // 是否有备查资料
    private Boolean referenceBackupFlag;

    // 备注
    private String remarks;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 有效标记默认值
    private Boolean enabledFlag;

    private Long id;


}
