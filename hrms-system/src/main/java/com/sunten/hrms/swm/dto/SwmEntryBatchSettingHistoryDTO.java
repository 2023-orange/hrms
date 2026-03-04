package com.sunten.hrms.swm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2020-11-24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SwmEntryBatchSettingHistoryDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // id主键
    private Long id;

    // 所得期间（格式：年.月）
    private String incomePeriod;

    // 工牌号
    private String workCard;

    // 条目
    private String entryName;

    // 金额
    private Double price;

    // 操作描述
    private String description;

    // 创建人
    private String empName;
}
