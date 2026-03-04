package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcReqFlowdata {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id; //id
    private String typeCode; //申请单种类编号
    private String flowCode; //流程编号
    private String variableKey; //名称
    private String variableName;
    private String variable; //内容
    private String valueType; //内容类型
    private Boolean deleteFlag; //删除标识
    private String createBy; //创建人
    private java.util.Date createTime; //创建时间
    private String modifyBy; //修改人
    private java.util.Date modifyTime; //修改时间
    private Boolean nodeFlag; //是否流程节点标识

}
