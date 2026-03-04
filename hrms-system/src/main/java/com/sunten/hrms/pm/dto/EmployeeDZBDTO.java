package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.TableName;
    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import java.time.LocalDateTime;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-09-09
 */
@Getter
@Setter
@ToString(callSuper = true)
public class EmployeeDZBDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private String kh;

    private String gh;

    private Integer xjbs;

    private Integer clbs;

    private String xm;

    private Integer id;

    private String bh;

    private LocalDateTime sj;


}
