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
 * @since 2020-10-26
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndInterfaceOperationRecordDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;


    // 接口底层名名（真实名称放集值
    private String operationValue;

    // 是否成功（1成功，0失败）
    private Boolean successFlag;

    // 数据处理描述
    private String dataProcessingDescription;

    // 操作描述
    private String operationDescription;


}
