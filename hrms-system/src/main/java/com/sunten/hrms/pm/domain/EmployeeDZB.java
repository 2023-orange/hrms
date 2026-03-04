package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * @author liangjw
 * @since 2021-09-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("employeeDZB")
public class EmployeeDZB extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String kh;

    private String gh;

    private Integer xjbs;

    private Integer clbs;

    private String xm;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Integer id;

    private String bh;

    private LocalDateTime sj;


}
