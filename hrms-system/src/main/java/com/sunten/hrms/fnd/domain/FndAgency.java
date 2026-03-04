package com.sunten.hrms.fnd.domain;

import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.fnd.vo.FndAgencyVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class FndAgency extends BaseEntity {
    private static final long serialVersionUID = 1L;
    // 代办描述，存个人、管理
    private String description;
    // 跳转组件名
    private String componentName;
    // 需要的参数
    private String params;
    // 子
    private List<FndAgencyVo> children;
    //    // 类别标识
//    private String type;
    // 节点id
    private Long id;
    // 父节点id
    private Long parentId;
    // 数量
    private Long count;
}
