package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import java.time.LocalTime;
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
public class AcRegimeTimeDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id
    private Long id;

    // 排班时间开始
    private LocalTime timeFrom;

    // 排班时间结束
    private LocalTime timeTo;

    // 是否跨日
    private Boolean extendTimeFlag;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 有效标记
    private Boolean enabledFlag;


}
