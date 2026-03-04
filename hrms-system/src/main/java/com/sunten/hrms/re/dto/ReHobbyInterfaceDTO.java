package com.sunten.hrms.re.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
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
public class ReHobbyInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 招骋ID
//    private Long reId;
    private ReRecruitmentInterfaceDTO recruitmentInterface;

    // 技能爱好
    private String hobby;

    // 级别
    private String levelMyself;

    // 认证等级
    private String levelMechanism;

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
