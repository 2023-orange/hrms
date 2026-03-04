package com.sunten.hrms.pm.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author liangjw
 * @since 2021-10-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
@TableName("DXNJ")
public class Dxnj extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 年度
     */
    @TableField("ND")
    private Integer nd;

    /**
     * 工牌号
     */
    @TableField("GPH")
    private String gph;

    /**
     * 姓名
     */
    @TableField("XM")
    private String xm;

    @TableField("BMMC")
    private String bmmc;

    @TableField("KSMC")
    private String ksmc;

    @TableField("team")
    private String team;

    @TableField("RZSJ")
    private LocalDateTime rzsj;

    @TableField("GL")
    private Integer gl;

    @TableField("NJTS")
    private Float njts;

    @TableField("YXJTS")
    private Float yxjts;

    @TableField("SYXJTS")
    private Float syxjts;


    @TableField("JHXJSJ")
    private String jhxjsj;

    /**
     * 实际休假时间（废弃）
     */
    @TableField("SJXJSJ")
    private String sjxjsj;

    /**
     * （废弃）
     */
    @TableField("BZ")
    private String bz;

    /**
     * 年假延续天数
     */
    private Float durationAnnualLeave;

    @TableId(value = "ID", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Integer id;

    @TableField("ZHXGR")
    private String zhxgr;

    @TableField("ZHXGSJ")
    private LocalDateTime zhxgsj;

    private Float realAnnualLeave;

    private LocalDate tenYearDate;

    private LocalDate twentyYearDate;

    private Long gssl;

    private Long sbgl;

    private Boolean checkFlag;

    private Boolean tenYearChangeFlag;

    private Boolean twentyYearChangeFlag;


}
