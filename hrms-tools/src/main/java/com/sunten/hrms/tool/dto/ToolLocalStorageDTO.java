package com.sunten.hrms.tool.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2019-12-25
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ToolLocalStorageDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String realName;

    // 文件名
    private String name;

    // 后缀
    private String suffix;

    // 路径
    private String path;

    // 类型
    private String type;

    // 大小
    private String fileSize;

    // 操作人
    private String operate;


}
