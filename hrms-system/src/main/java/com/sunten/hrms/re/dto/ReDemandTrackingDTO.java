package com.sunten.hrms.re.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2022-01-18
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ReDemandTrackingDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 需求id
    private Long demandId;

    // 过程记录
    private String reContent;

    private Long id;

    private String editBy;

    private Boolean enabledFlag;


}
