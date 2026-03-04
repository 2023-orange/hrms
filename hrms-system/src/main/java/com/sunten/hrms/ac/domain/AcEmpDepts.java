package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.swm.domain.SwmConsolationMoney;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 考勤模块人员数据权限范围表
 * </p>
 *
 * @author liangjw
 * @since 2020-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcEmpDepts extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 对应fnd_role 目前只考虑资料员
     */
    @NotNull
    private Long roleId;

    /**
     * 对应人事主表id
     */
    @NotNull
    private Long employeeId;

    /**
     * 部门id
     */
    @NotNull
    private Long deptId;

    /**
     * 有效标识
     */
    @NotNull
    private Boolean enabledFlag;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private List<Long> deptList;

    private String deptName;

    private Long parentId;

    private String dataType;

    private String email;

    // 方便邮件发送
    private List<SwmConsolationMoney> swmConsolationMonies;




}
