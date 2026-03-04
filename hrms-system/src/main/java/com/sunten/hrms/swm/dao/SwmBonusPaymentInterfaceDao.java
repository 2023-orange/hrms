package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmBonusPaymentInterface;
import com.sunten.hrms.swm.dto.SwmBonusPaymentInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 奖金发放接口表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmBonusPaymentInterfaceDao extends BaseMapper<SwmBonusPaymentInterface> {

    int insertAllColumn(SwmBonusPaymentInterface bonusPaymentInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmBonusPaymentInterface bonusPaymentInterface);

    int updateAllColumnByKey(SwmBonusPaymentInterface bonusPaymentInterface);

    SwmBonusPaymentInterface getByKey(@Param(value = "id") Long id);

    List<SwmBonusPaymentInterface> listAllByCriteria(@Param(value = "criteria") SwmBonusPaymentInterfaceQueryCriteria criteria);

    List<SwmBonusPaymentInterface> listAllByCriteriaPage(@Param(value = "page") Page<SwmBonusPaymentInterface> page, @Param(value = "criteria") SwmBonusPaymentInterfaceQueryCriteria criteria);

    int insertByExcel(SwmBonusPaymentInterface bonusPaymentInterface);

    List<SwmBonusPaymentInterface> getBonusPaymentSummaryByImportList(@Param(value = "workCards")Set<String> workCards, @Param(value = "groupIds")Set<Long> groupIds);

    Boolean ifExistsRecord(@Param(value = "workCard") String workCard,@Param(value = "bonusId") Long bonusId);
}
