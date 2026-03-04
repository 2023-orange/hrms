package com.sunten.hrms.td.dto;

    import java.math.BigDecimal;
    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.pm.dto.PmEmployeeDTO;
    import com.sunten.hrms.tool.domain.ToolLocalStorage;
    import com.sunten.hrms.tool.dto.ToolLocalStorageDTO;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xukai
 * @since 2021-06-16
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdTeacherDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 讲师ID
//    private Long employeeId;
    private PmEmployeeDTO pmEmployee;
    // 课程主题
    private String title;

    // 课程内容
    private String content;

    // 授课经验（年）: 他司+我司
    private BigDecimal teachExperience;

    // 他司经验年限
    private BigDecimal otherExperienceCompose;

    // 我司经验年限
    private BigDecimal myExperienceCompose;

    // 曾授课课题
    private String everExperience;

    // 擅长领域
    private String beGoodAt;

    // 讲师等级
    private String level;

    // 讲师分数
    private BigDecimal score;

    // 认证时间
    private LocalDateTime passDate;

    // 来源：线上申请、线下录入
    private String attribute1;

    // 记录OA单号，如果是线上申请产生的
    private String attribute2;

    private String attribute5;

    private String attribute4;

    private Long id;

    private String attribute3;
}
