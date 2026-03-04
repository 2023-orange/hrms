package com.sunten.hrms.fnd.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2022-08-12
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndSuperQueryGroupDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String groupName;

    private String remark;

    private Boolean enabledFlag;


}
