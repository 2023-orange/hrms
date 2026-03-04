package com.sunten.hrms.cm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sunten.hrms.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 * 工衣明细表
 * </p>
 *
 * @author liangjw
 * @since 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class CmDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 人员id
     */
    @NotNull
    private Long employeeId;

    private String name;

    private String workCard;

    private String gender;

    /**
     * 有效标记
     */
    @NotNull
    private Boolean enabledFlag;

    /**
     * 生成类别
     */
    @NotBlank
    private String type;

    /**
     * 年份
     */
    @NotNull
    private Integer year;

    /**
     * 部门（毕竟属于历史记录，适当冗余）
     */
    private String dept;

    /**
     * 科室
     */
    private String department;

    /**
     * 班组
     */
    private String team;

    /**
     * 岗位
     */
    private String job;

    /**
     * 成本中心号
     */
    private String costCenterNum;

    /**
     * 审批状态（notCommit、documenter、leader、complete）
     */
    @NotBlank
    private String status;

    /**
     * 导出标记
     */
    @NotNull
    private Boolean exportFlag;

    /**
     * 西服外套
     */
    private Integer suit1;

    /**
     * 西服马甲
     */
    private Integer suit2;

    /**
     * 西服裤
     */
    private Integer suit3;

    /**
     * 西服裙
     */
    private Integer suit4;

    /**
     * 科服短衬衣
     */
    private Integer familyClothing1;

    /**
     * 科服长衬衣
     */
    private Integer familyClothing2;

    /**
     * 科服裤
     */
    private Integer familyClothing3;

    /**
     * 科服裙
     */
    private Integer familyClothing4;

    /**
     * 夏工服（夏牛仔短袖）
     */
    private Integer summerWorkingClothes1;

    /**
     * 夏工服（夏牛仔长袖）
     */
    private Integer summerWorkingClothes2;

    /**
     * 夏裤
     */
    private Integer summerWorkingClothes3;

    /**
     * 冬工服(东上衣)
     */
    private Integer winterWorkingClothes1;

    /**
     * 冬工服(冬裤)
     */
    private Integer winterWorkingClothes2;

    /**
     * 售后服装（反光马甲）
     */
    private Integer postSaleClothing1;

    /**
     * 售后服装（售后款上衣）
     */
    private Integer postSaleClothing2;

    /**
     * 售后服装（售后款裤）
     */
    private Integer postSaleClothing3;

    /**
     * 售后服装（售后款冬上衣）
     */
    private Integer postSaleClothing4;

    /**
     * 售后服装（售后款冬裤）
     */
    private Integer postSaleClothing5;

    /**
     * 饭堂服装（长袖）
     */
    private Integer diningClothes1;

    /**
     * 饭堂服装（短袖）
     */
    private Integer diningClothes2;

    // 保安人员专用服(夏装短袖)
    private Integer securityClothes1;

    // 保安人员专用服(夏装裤)
    private Integer securityClothes2;

    // 保安人员专用服(冬装上衣)
    private Integer securityClothes3;

    // 保安人员专用服(冬装裤)
    private Integer securityClothes4;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private String attribute2;

    private String attribute3;

    private String attribute1;

    private String attribute4;

    private String clothesRemark;

    // 领导标记
    private Boolean leaderFlag;

    private Boolean removePropFlag;

    private LocalDateTime exportTime;

    private Long detailId;

    private Integer historyFlag;
}
