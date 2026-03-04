package com.sunten.hrms.ac.domain;

import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class AcEmployeeAttendanceCollect  extends BaseEntity { // 人员排班保存时使用的对象
    /**
     *  @author：liangjw
     *  @Date: 2020/10/10 9:07
     *  @Description: 人员id集合
     */
    private List<Long> empIds;

    /**
     *  @author：liangjw
     *  @Date: 2020/10/10 9:09
     *  @Description: 日期集合
     */
    private List<LocalDate> dates;

    /**
     *  @author：liangjw
     *  @Date: 2020/10/10 9:09
     *  @Description: 此对象记录排班信息
     */
    private AcEmployeeAttendance acEmployeeAttendance;
}
