package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2020-10-20
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AcLeaveFormTempDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 工牌号
    private String workCard;

    // 开始日期时间
    private LocalDateTime startTime;

    // 结束日期时间
    private LocalDateTime endTime;

    private Long id;


}
