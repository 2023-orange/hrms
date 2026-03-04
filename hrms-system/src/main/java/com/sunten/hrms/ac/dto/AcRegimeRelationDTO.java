package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.ac.domain.AcRegime;
    import com.sunten.hrms.ac.domain.AcRegimeTime;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ljw
 * @since 2020-09-17
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AcRegimeRelationDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id
    private Long id;

    // 制度id
    private Long regimeaId;

    // 排班 id
    private Long regimeTimeId;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 有效标记
    private Boolean enabledFlag;

    // 排班制度
    private AcRegimeDTO acRegime;

    // 排班时间段
    private AcRegimeTimeDTO acRegimeTime;


}
