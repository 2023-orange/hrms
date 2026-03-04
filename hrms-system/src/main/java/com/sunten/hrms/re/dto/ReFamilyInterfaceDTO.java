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
public class ReFamilyInterfaceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 招骋ID
//    private Long reId;
    private ReRecruitmentInterfaceDTO recruitmentInterface;

    // 姓名
    private String name;

    // 关系
    private String relationship;

    // 单位
    private String company;

    // 职务
    private String post;

    // 电话
    private String tele;

    // 性别
    private String gender;

    // 出生日期
    private LocalDate birthday;

    // 手机
    private String mobilePhone;

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
