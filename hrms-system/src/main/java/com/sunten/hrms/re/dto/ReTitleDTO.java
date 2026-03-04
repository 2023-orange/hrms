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
public class ReTitleDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 招骋ID
//    private Long reId;
    private ReRecruitmentDTO recruitment;

    // 职称级别
    private String titleLevel;

    // 职称名称
    private String titleName;

    // 评定时间
    private LocalDate evaluationTime;

    // 是否最高职称
    private Boolean newTitleFlag;

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
