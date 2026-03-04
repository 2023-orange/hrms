package com.sunten.hrms.td.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2020-08-04
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdTrainDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 培训名称
    private String trainName;

    // 开始时间
    private LocalDateTime startTime;

    // 结束时间
    private LocalDateTime endTime;

    // 培训内容
    private String trainContent;

    // 培训类型
    private String trainType;

    // 讲师
    private String lecturer;

    // 课时
    private Double trainTime;

    // 培训地点
    private String trainAddress;

    // 培训单位
    private String trainCompany;

    // 所获证书
    private String certificate;

    // 讲师信息
    private String lecturerInformation;

    // 备注
    private String remarks;

    // 弹性域1
    private String attribute1;

    // 弹性域2
    private String attribute2;

    // 弹性域3
    private String attribute3;

    // 有效标记默认值
    private Boolean enabledFlag;

    private Long id;


}
