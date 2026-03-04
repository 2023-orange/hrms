package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmBonus;
import com.sunten.hrms.swm.dto.SwmBonusQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 奖金表	    Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmBonusDao extends BaseMapper<SwmBonus> {

    int insertAllColumn(SwmBonus bonus);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmBonus bonus);

    int updateAllColumnByKey(SwmBonus bonus);

    SwmBonus getByKey(@Param(value = "id") Long id);

    List<SwmBonus> listAllByCriteria(@Param(value = "criteria") SwmBonusQueryCriteria criteria);

    List<SwmBonus> listAllByCriteriaPage(@Param(value = "page") Page<SwmBonus> page, @Param(value = "criteria") SwmBonusQueryCriteria criteria);

    int deleteByEnabled(SwmBonus swmBonus);
}
