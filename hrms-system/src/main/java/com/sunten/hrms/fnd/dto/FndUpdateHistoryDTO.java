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
 * @since 2020-07-24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndUpdateHistoryDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id
    private Long id;

    // 表名
    private String tableName;

    // 列名
    private String columnName;

    // 表id
    private Long tableId;

    // 原值
    private String oldValue;

    // 修改值
    private String newValue;


}
