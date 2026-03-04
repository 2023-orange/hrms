package com.sunten.hrms.re.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2020-08-05
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ReAwardDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 招骋ID
//    private Long reId;
    private ReRecruitmentDTO recruitment;

    // 奖罚类别
    private String type;

    // 奖罚名称
    private String awardName;

    // 奖罚处理开始时间
    private LocalDate awardStarTime;

    // 奖罚处理结束时间
    private LocalDate awardEndTime;

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
