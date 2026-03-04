package com.sunten.hrms.ac.dto;

    import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xukai
 * @since 2021-12-22
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AcFakeRecordSettingDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 用户名
    private String userName;

    // 工牌号
    private String workCard;

    // 有效标记
    private Boolean enabledFlag;


}
