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
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author xukai
 * @since 2021-02-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndAuthorizationDts extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 授权表ID
     */
    @NotNull
    private Long authorizationId;

    private FndAuthorization authorization;

    /**
     * 授权部门ID
     */
    @NotNull
    private Long deptId;

    /**
     * 生效标记
     */
    @NotNull
    private Boolean enableFlag;

    @TableId(value = "id", type = IdType.NONE)
    @NotNull
    private Long id;

    private List<Long> addList; // 新增部门
    private List<Long> delList; // 失效部门

    private FndDept fndDept;
}
