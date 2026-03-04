package com.sunten.hrms.fnd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 接口操作记录表
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndInterfaceOperationRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 接口底层名名（真实名称放集值
     */
    @NotBlank
    private String operationValue;

    /**
     * 是否成功（1成功，0失败）
     */
    private Boolean successFlag;

    /**
     * 数据处理描述
     */
    private String dataProcessingDescription;

    /**
     * 操作描述
     */
    @NotBlank
    private String operationDescription;


}
