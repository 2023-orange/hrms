package com.sunten.hrms.fnd.vo;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FndAgencyVo {
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
