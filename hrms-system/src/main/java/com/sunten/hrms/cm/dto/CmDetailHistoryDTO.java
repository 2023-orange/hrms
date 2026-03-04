package com.sunten.hrms.cm.dto;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.sunten.hrms.base.BaseEntity;
import com.sunten.hrms.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhoujy
 * @since 2023-02-23
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CmDetailHistoryDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long detailId;

    private Long employeeId;

    private Boolean enabledFlag;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String type;

    private Integer year;

    private String dept;

    private String department;

    private String team;

    private String job;

    private String costCenterNum;

    private String status;

    private Boolean exportFlag;

    private Integer suit1;

    private Integer suit2;

    private Integer suit3;

    private Integer suit4;

    private Integer familyClothing1;

    private Integer familyClothing2;

    private Integer familyClothing3;

    private Integer familyClothing4;

    private Integer summerWorkingClothes1;

    private Integer summerWorkingClothes2;

    private Integer summerWorkingClothes3;

    private Integer winterWorkingClothes1;

    private Integer winterWorkingClothes2;

    private Integer postSaleClothing1;

    private Integer postSaleClothing2;

    private Integer postSaleClothing3;

    private Integer postSaleClothing4;

    private Integer postSaleClothing5;

    private Integer diningClothes1;

    private Integer diningClothes2;

    private Integer securityClothes1;

    private Integer securityClothes2;

    private Integer securityClothes3;

    private Integer securityClothes4;

    private String clothesRemark;

    private String updateByStr;
}
