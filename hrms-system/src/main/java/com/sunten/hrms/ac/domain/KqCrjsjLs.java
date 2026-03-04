package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2020-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("KQ_CRJSJ_LS")
public class KqCrjsjLs extends BaseEntity {

    private static final long serialVersionUID = 1L;

//    @TableId(value = "date8", type = IdType.NONE)
    @NotNull
    private LocalDateTime date8;

//    @TableField("ID")
    @NotNull
    private Integer id;

    private String syqk;

//    @TableId(value = "kh", type = IdType.NONE)
    @NotNull
    private String kh;

    private String crjqk;

    private Integer mjjbh;

//    @TableId(value = "mjkzqbh", type = IdType.NONE)
    @NotNull
    private String mjkzqbh;

//    @TableId(value = "time8", type = IdType.NONE)
    @NotNull
    private LocalDateTime time8;

    private Boolean acstosql;


}
