package com.sunten.hrms.fnd.domain;

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
 * 超级查询数据临时表
 * </p>
 *
 * @author liangjw
 * @since 2021-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndSuperQueryTemp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 列名(中文)
     */
    @NotBlank
    private String colNameChn;

    /**
     * 列名
     */
    @NotBlank
    private String colName;

    /**
     * 所属类
     */
    @NotBlank
    private String type;

    /**
     * 员工id
     */
    @NotNull
    private Long employeeId;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private int order;

    private String name;

    private String workCard;

    private Long searchUserId;

    public FndSuperQueryTemp(@NotBlank String colNameChn, @NotBlank String colName, @NotBlank String type, @NotNull Long employeeId, int order,@NotNull Long searchUserId) {
        this.colNameChn = colNameChn;
        this.colName = colName;
        this.type = type;
        this.employeeId = employeeId;
        this.order = order;
        this.searchUserId = searchUserId;
    }

    public FndSuperQueryTemp() {

    }

}
