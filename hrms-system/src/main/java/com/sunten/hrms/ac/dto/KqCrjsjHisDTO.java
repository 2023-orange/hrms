package com.sunten.hrms.ac.dto;

    import com.baomidou.mybatisplus.annotation.TableName;
    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2020-10-19
 */
@Getter
@Setter
@ToString(callSuper = true)
public class KqCrjsjHisDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private String mjkzqbh;

    private LocalDateTime time8;

    private Boolean acstosql;

    private String crjqk;

    private Integer mjjbh;

    private String kh;

    private Integer id;

    private LocalDateTime date8;

    private String syqk;


}
