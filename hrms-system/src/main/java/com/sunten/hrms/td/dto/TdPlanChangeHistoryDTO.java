package com.sunten.hrms.td.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-06-16
 */
@Getter
@Setter
@ToString(callSuper = true)
public class TdPlanChangeHistoryDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 父id
    private Long parentId;

    // 变更类别(change、cancel、implement)
    private String changeType;

    // 拟更改时间段
    private String changePlanDate;

    // 变更原因
    private String changeDescribe;

    // 申请单号
    private String oaOrder;

    private Long id;

    private Boolean passFlag;


}
