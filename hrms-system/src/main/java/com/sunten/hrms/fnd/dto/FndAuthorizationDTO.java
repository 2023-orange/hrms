package com.sunten.hrms.fnd.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.fnd.domain.FndAuthorizationDts;
    import com.sunten.hrms.fnd.domain.FndDept;
    import com.sunten.hrms.pm.domain.PmEmployee;
    import com.sunten.hrms.pm.dto.PmEmployeeDTO;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    import java.time.LocalDate;
    import java.util.List;

/**
 * @author xukai
 * @since 2021-01-29
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndAuthorizationDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 授权人
    private Long authorizationBy;

    private PmEmployee byEmployee;

    // 被授权人
    private Long authorizationTo;

    private PmEmployeeDTO toEmployee;

    // 授权类型
    private String authorizationType;

    // 授权起始日期
    private LocalDate beginDate;

    // 授权截止日期
    private LocalDate endDate;

    // 生效标识
    private Boolean enableFlag;

    private Long id;

    private List<FndAuthorizationDtsDTO> fndAuthorizationDts;
}
