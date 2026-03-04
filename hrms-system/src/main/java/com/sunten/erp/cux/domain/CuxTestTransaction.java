package com.sunten.erp.cux.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Data
@ToString
@Accessors(chain = true)
public class CuxTestTransaction {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private BigDecimal id;

    private String description;

    private LocalDateTime creationDate;


}
