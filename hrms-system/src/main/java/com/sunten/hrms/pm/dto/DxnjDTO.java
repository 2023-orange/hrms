package com.sunten.hrms.pm.dto;

    import com.baomidou.mybatisplus.annotation.TableName;
    import com.baomidou.mybatisplus.annotation.IdType;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.baomidou.mybatisplus.annotation.TableField;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangjw
 * @since 2021-10-08
 */
@Getter
@Setter
@ToString(callSuper = true)
public class DxnjDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    // 年度
    private Integer nd;

    // 工牌号
    private String gph;

    // 姓名
    private String xm;

    // （废弃）
    private String bmmc;

    // （废弃）
    private String ksmc;

    private String team;

    // （废弃，直接从入职表获取）
    private LocalDateTime rzsj;

    // （废弃）
    private Integer gl;

    // 年假天数（废弃，使用pm_employee表上的paid_annual_leave）
    private Float njts;

    // 已休假天数（废弃，根据OA的计算直接获取）
    private Float yxjts;

    // 剩余休假天数（废弃，直接计算）
    private Float syxjts;

    // 计划休假时间（废弃）
    private String jhxjsj;

    // 实际休假时间（废弃）
    private String sjxjsj;

    // （废弃）
    private String bz;

    // 年假延续天数
    private Float durationAnnualLeave;

    private Integer id;

    private String zhxgr;

    private LocalDateTime zhxgsj;

    private Float realAnnualLeave;

    private LocalDate tenYearDate;

    private LocalDate twentyYearDate;

    private Long gssl;

    private Long sbgl;

    private Boolean checkFlag;

}
