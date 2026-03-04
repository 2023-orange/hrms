package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import java.util.Set;

    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
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
public class PmEmployeeFamilyDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 员工id
//    private Long employeeId;
    private PmEmployeeDTO employee;

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

    Set<PmEmployeeFamilyDTO> children;
    //修改标记，前台使用
    private Boolean setCheck = false;

    private String idKey;

    private String checkFlag;
}
