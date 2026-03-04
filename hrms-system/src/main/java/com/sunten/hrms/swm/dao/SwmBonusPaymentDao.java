package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmBonus;
import com.sunten.hrms.swm.domain.SwmBonusPayment;
import com.sunten.hrms.swm.dto.SwmBonusPaymentQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.swm.vo.SelfBonusListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 奖金发放表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmBonusPaymentDao extends BaseMapper<SwmBonusPayment> {

    int insertAllColumn(SwmBonusPayment bonusPayment);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmBonusPayment bonusPayment);

    int updateAllColumnByKey(SwmBonusPayment bonusPayment);

    SwmBonusPayment getByKey(@Param(value = "id") Long id);

    List<SwmBonusPayment> listAllByCriteria(@Param(value = "criteria") SwmBonusPaymentQueryCriteria criteria);

    List<SwmBonusPayment> listAllByCriteriaPage(@Param(value = "page") Page<SwmBonusPayment> page, @Param(value = "criteria") SwmBonusPaymentQueryCriteria criteria);

    List<SelfBonusListVo> listSelfByCriteriaPage(@Param(value = "page") Page<SelfBonusListVo> page, @Param(value = "criteria") SwmBonusPaymentQueryCriteria criteria);

    int generateBonusPayment(SwmBonus swmBonus);

    int deleteByBonusId(SwmBonus swmBonus);

    Integer countByBonusId(@Param(value = "bonusId")Long bonusId);

    void insertByInterface(@Param(value = "groupId") Long groupId, @Param(value = "userId") Long userId, @Param(value = "bonusId") Long bonusId);

    void updateByInterface(@Param(value = "groupId") Long groupId, @Param(value = "userId") Long userId);

    List<SwmBonusPayment> exportBonusPaymentByBonusId(@Param(value = "id")Long id);

    void grantAllBonus(@Param(value = "bonusId")Long bonusId, @Param(value = "grantFlag")Boolean grantFlag);
}
