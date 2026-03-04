package com.sunten.hrms.ac.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcReqParameter{

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id; //id
    private String keyWord; //关键字
    private String valueField; //显示内容
    private String textField; //存储内容
    private String fatherId; //父节点
    private String sequenceNumber; //序号
    private Boolean deleteFlag; //删除标识
    private String createBy; //创建人
    private java.util.Date createTime; //创建时间
    private String modifyBy; //修改人
    private java.util.Date modifyTime; //修改时间

}
