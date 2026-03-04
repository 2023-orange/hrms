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
public class ReWorkhistoryInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 招骋ID
//    private Long reId;
    private ReRecruitmentInterfaceDTO recruitmentInterface;

    // 单位
    private String company;

    // 职务
    private String post;

    // 开始时间
    private LocalDate startTime;

    // 结束时间
    private LocalDate endTime;

    // 月薪
    private Double salaryOld;

    // 离职原因
    private String reasonsLeaving;

    // 证明人
    private String witness;

    // 联系电话
    private String tele;

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
