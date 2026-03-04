package com.sunten.hrms.fnd.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2020-10-15
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndDeptSnapshotDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id
    private Long id;

    // 日期
    private LocalDate date;

    // 节点id
    private Long deptId;

    // 节点名称
    private String deptName;

    // 父级节点id
    private Long parentId;

    // 节点代码
    private String deptCode;

    // 所属层级
    private String deptLevel;

    // 节点序号
    private Long deptSequence;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 弹性域4
    private String attribute4;

    // 弹性域5
    private String attribute5;


    private Long adminJobId;


}
