package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.ac.domain.AcDeptAttendance;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.fnd.domain.FndDeptSnapshot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 部门科室岗位关系快照
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PmEmployeeJobSnapshot extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    @NotNull
    private LocalDate date;

    /**
     * 员工ID
     */
    @NotNull
    private Long employeeId;

    /**
     * 岗位ID
     */
    @NotNull
    private Long jobId;

    @NotNull
    private Boolean workAttendanceFlag;

    @NotNull
    private Boolean workshopAttendanceFlag;

    /**
     * 部门科室ID
     */
    @NotNull
    private Long deptId;
    private FndDeptSnapshot fndDeptSnapshot;

    private AcDeptAttendance attendance;

    /**
     * 弹性域1
     */
    private String attribute1;

    /**
     * 弹性域2
     */
    private String attribute2;

    /**
     * 弹性域3
     */
    private String attribute3;

    /**
     * 弹性域4
     */
    private String attribute4;

    /**
     * 弹性域5
     */
    private String attribute5;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;


}
