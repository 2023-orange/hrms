package com.sunten.hrms.fnd.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author batan
 * @since 2019-12-20
 */
@Data
@ToString
@Accessors(chain = true)
public class FndVisit {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private String date;

    private Long ipCounts;

    private Long pvCounts;

    private String weekDay;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public @interface Update {
    }

}
