package com.sunten.hrms.ac.domain;

import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
/**
 *  @author：liangjw
 *  @Date: 2020/9/24 11:52
 *  @Description: 生成日历明细时使用的对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcCalendarHeaderAndYear extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     *  @author：liangjw
     *  @Date: 2020/9/24 11:50
     *  @Description: 生成的年份
     */
    @NotNull
    private LocalDate year;

    /**
     *  @author：liangjw
     *  @Date: 2020/9/24 11:51
     *  @Description: 生成日历的headerId
     */
    @NotNull
    private Long calendarHeaderId;

}
