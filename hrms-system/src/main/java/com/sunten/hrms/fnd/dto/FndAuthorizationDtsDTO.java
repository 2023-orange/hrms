package com.sunten.hrms.fnd.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.fnd.domain.FndAuthorizationDts;
    import com.sunten.hrms.fnd.domain.FndDept;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.util.List;

/**
 * @author xukai
 * @since 2021-02-02
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndAuthorizationDtsDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 授权表ID
    private Long authorizationId;

    private FndAuthorizationDTO authorization;

    // 授权部门ID
    private Long deptId;

    // 生效标记
    private Boolean enableFlag;

    private Long id;

    private FndDeptDTO fndDept;

}
