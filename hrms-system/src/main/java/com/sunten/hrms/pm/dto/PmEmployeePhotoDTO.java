package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xukai
 * @since 2020-09-09
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmEmployeePhotoDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 真实文件名
    private String realName;

    // 路径
    private String path;

    // 图像大小
    private String avaterSize;

    private Long id;


}
