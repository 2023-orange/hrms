package com.sunten.hrms.re.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.re.domain.ReRecruitment;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xukai
 * @since 2020-08-28
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ReVocationalDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 招聘id
    private ReRecruitment recruitment;

    // 资格名称
    private String vocationalName;

    // 级别
    private String vocationalLevel;

    // 评定时间
    private LocalDate evaluationTime;

    // 发证机构
    private String evaluationMechanism;

    // 有效期
    private LocalDate vocationalValidity;

    // 备注
    private String remarks;

    // 是否当前最高
    private Boolean newVocationalFlag;

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
