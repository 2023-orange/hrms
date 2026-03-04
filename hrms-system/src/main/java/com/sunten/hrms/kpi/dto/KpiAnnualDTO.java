package com.sunten.hrms.kpi.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhoujy
 * @since 2023-11-27
 */
@Getter
@Setter
@ToString(callSuper = true)
public class KpiAnnualDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 主键
    private Long id;

    // 年份
    private String year;

    // 考核状态
    private String examineStatus;

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


}
