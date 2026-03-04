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
 * 体检项目关联表
 * </p>
 *
 * @author xukai
 * @since 2021-04-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmMedicalRelevance extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位体检表id
     */
    @NotNull
    private Long medicalJobId;

    /**
     * 体检项id(实体)
     */
//    @NotNull
//    private Long projectId;

    private PmMedicalProject medicalProject;

    /**
     * 体检项目类别（上岗体检项目、在岗体检项目、离岗体检项目）
     */
    @NotBlank
    private String projectType;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


}
