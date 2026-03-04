package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdPlanImplement;
import com.sunten.hrms.td.dto.TdPlanImplementQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 计划实施申请 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-05-25
 */
@Mapper
@Repository
public interface TdPlanImplementDao extends BaseMapper<TdPlanImplement> {

    int insertAllColumn(TdPlanImplement planImplement);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdPlanImplement planImplement);

    int updateAllColumnByKey(TdPlanImplement planImplement);

    TdPlanImplement getByKey(@Param(value = "id") Long id);

    List<TdPlanImplement> listAllByCriteria(@Param(value = "criteria") TdPlanImplementQueryCriteria criteria);

    List<TdPlanImplement> listAllByCriteriaPage(@Param(value = "page") Page<TdPlanImplement> page, @Param(value = "criteria") TdPlanImplementQueryCriteria criteria);

    List<TdPlanImplement> selectByPlanId(@Param(value = "planId")Long planId);

    TdPlanImplement getByPlanIdForTemplate(@Param(value = "id")Long id);
}
