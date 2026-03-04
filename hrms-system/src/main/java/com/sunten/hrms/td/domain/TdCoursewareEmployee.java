package com.sunten.hrms.td.domain;

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
 * @since 2022-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class TdCoursewareEmployee extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 课件id
     */
    @NotNull
    private Long coursewareId;

    /**
     * 选中人员id
     */
    @NotNull
    private Long employeeId;

    /**
     * 选中人员姓名
     */
    @NotBlank
    private String employeeName;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


}
