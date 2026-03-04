package com.sunten.hrms.swm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-02-24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmEmpDeptDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 薪酬人员id
    private Long seId;

    // 管辖部门
    private String department;

    // 管辖科室
    private String administrativeOffice;

    // 管辖班组
    private String team;

    // 范围类别（目前只有月度考核）
    private String type;

    private Boolean enabledFlag;

    private SwmEmployeeDTO swmEmployee;;

}
