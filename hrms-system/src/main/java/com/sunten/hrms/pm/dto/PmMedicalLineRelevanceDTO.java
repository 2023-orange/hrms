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
 * @since 2021-04-20
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PmMedicalLineRelevanceDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 体检申请子表id
    private Long medicalLineId;

    // 体检项目名称
    private String projectName;

    private Long id;


}
