package com.sunten.hrms.swm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * <p>
 * 薪酬人员管理范围
 * </p>
 *
 * @author liangjw
 * @since 2021-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SwmEmpDept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 薪酬人员id
     */
    @NotNull
    private Long seId;

    /**
     * 管辖部门
     */
    @NotBlank
    private String department;

    /**
     * 管辖科室
     */
    private String administrativeOffice;

    /**
     * 管辖班组
     */
    private String team;

    /**
     * 范围类别（目前只有月度考核）
     */
    @NotBlank
    private String type;

    @NotNull
    private Boolean enabledFlag;

    private SwmEmployee swmEmployee;


    private Set<String> deptCodeList;


}
