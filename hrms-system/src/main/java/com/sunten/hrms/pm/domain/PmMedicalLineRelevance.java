package com.sunten.hrms.pm.domain;

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
 *
 * </p>
 *
 * @author xukai
 * @since 2021-04-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmMedicalLineRelevance extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 体检申请子表id
     */
    @NotNull
    private Long medicalLineId;

    /**
     * 体检项目名称
     */
    @NotNull
    private String projectName;

    @TableId(value = "id", type = IdType.NONE)
    @NotNull
    private Long id;


}
