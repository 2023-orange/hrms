package com.sunten.hrms.fnd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.ac.domain.AcDeptAttendance;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 组织架构快照
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndDeptSnapshot extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 日期
     */
    @NotNull
    private LocalDate date;

    /**
     * 节点id
     */
    @NotNull
    private Long deptId;

    /**
     * 节点名称
     */
    private String deptName;

    /**
     * 父级节点id
     */
    @NotNull
    private Long parentId;

    /**
     * 节点代码
     */
    @NotBlank
    private String deptCode;

    /**
     * 所属层级
     */
    private String deptLevel;

    /**
     * 节点序号
     */
    @NotNull
    private Long deptSequence;

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

    private AcDeptAttendance attendance;

    private Long attendanceId;

    private String dept;

    private String department;

    private String team;

    private Long adminJobId;


}
