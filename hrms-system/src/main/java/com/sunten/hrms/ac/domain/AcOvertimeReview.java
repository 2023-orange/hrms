package com.sunten.hrms.ac.domain;

import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
/**
 *  @author：liangjw
 *  @Date: 2020/12/10 11:37
 *  @Description: OA的考勤权限表
 */
public class AcOvertimeReview extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String department;
    private String administrativeOffice;
    private String name;
    private String workCard;
    private Long deleteFlag;
}
