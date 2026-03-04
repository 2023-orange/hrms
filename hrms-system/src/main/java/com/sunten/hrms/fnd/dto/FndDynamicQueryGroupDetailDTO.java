package com.sunten.hrms.fnd.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
    import com.sunten.hrms.fnd.domain.FndDynamicQueryGroup;
    import com.sunten.hrms.fnd.domain.FndDynamicQueryOperatorGroup;
    import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2022-07-29
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndDynamicQueryGroupDetailDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

//    private Long groupId;
    private FndDynamicQueryGroupDTO dynamicQueryGroup;

    private String label;

    private String queryTable;

    private String field;

    private String fieldType;

    private String fieldTypeDesc;

    private String jdbcType;

//    private Long operatorGroupId;
    private FndDynamicQueryOperatorGroupDTO dynamicQueryOperatorGroup;

    private String component;

    private String componentAttributes;

    private String listType;

    private String listAttributes;

    private String regularExpression;

    private String specialSql;

    private Integer sort;

    private Boolean enabledFlag;


}
