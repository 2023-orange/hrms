package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
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
 * @author liangjw
 * @since 2020-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("KQ_CRJSJ_HIS")
public class KqCrjsjHis extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String mjkzqbh;

    private LocalDateTime time8;

    @NotNull
    private Boolean acstosql;

    private String crjqk;

    private Integer mjjbh;

    private String kh;

    @NotNull
    private Integer id;

    private LocalDateTime date8;

    private String syqk;


}
