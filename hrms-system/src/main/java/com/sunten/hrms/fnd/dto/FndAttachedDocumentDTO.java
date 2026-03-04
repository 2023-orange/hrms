package com.sunten.hrms.fnd.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author batan
 * @since 2020-09-25
 */
@Getter
@Setter
@ToString(callSuper = true)
public class FndAttachedDocumentDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 真实文件名
    private String realName;

    // 路径
    private String path;

    // 图像大小
    private String avaterSize;

    private String source;

    private Long sourceId;

    private String type;

    private boolean enabledFlag;

    private Long id;

}
