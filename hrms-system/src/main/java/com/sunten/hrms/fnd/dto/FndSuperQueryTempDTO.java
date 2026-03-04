package com.sunten.hrms.fnd.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-08-19
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndSuperQueryTempDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 列名(中文)
    private String colNameChn;

    // 列名
    private String colName;

    // 所属类
    private String type;

    // 员工id
    private Long employeeId;

    private Long id;

    private String name;

    private String workCard;

    private Long searchUserId;


}
