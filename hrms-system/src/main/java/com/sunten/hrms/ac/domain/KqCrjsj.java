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
@TableName("KQ_CRJSJ")
public class KqCrjsj extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer mjjbh;

    private String kh;

    private String crjqk;

    private LocalDateTime time8;

    private Boolean acstosql;

    private String mjkzqbh;

    private String syqk;

    @TableId(value = "ID",type = IdType.AUTO)
    private Integer ID;

    private LocalDateTime date8;

    private Boolean inFlag;



}
