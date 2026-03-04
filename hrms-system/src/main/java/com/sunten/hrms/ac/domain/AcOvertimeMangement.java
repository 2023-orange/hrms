package com.sunten.hrms.ac.domain;

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
 * @author zouyp
 * @since 2023-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcOvertimeMangement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.NONE)
    @NotNull
    private Long id;

    /**
     * 部门id
     */
    private Integer deptId;

    /**
     * 总人数
     */
    private Integer totalNumber;

    /**
     * 部门人均加班工时
     */
    private Float averageOvertimeHour;

    /**
     * 系统限制时数
     */
    private Float systemLimitHour;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    /**
     * 状态（0失效，1生效）
     */
    private Integer enabledFlag;


}
