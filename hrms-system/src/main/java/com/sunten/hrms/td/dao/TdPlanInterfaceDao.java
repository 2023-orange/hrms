package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdPlanInterface;
import com.sunten.hrms.td.dto.TdPlanInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训计划接口表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-05-23
 */
@Mapper
@Repository
public interface TdPlanInterfaceDao extends BaseMapper<TdPlanInterface> {

    int insertAllColumn(TdPlanInterface planInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdPlanInterface planInterface);

    int updateAllColumnByKey(TdPlanInterface planInterface);

    TdPlanInterface getByKey(@Param(value = "id") Long id);

    List<TdPlanInterface> listAllByCriteria(@Param(value = "criteria") TdPlanInterfaceQueryCriteria criteria);

    List<TdPlanInterface> listAllByCriteriaPage(@Param(value = "page") Page<TdPlanInterface> page, @Param(value = "criteria") TdPlanInterfaceQueryCriteria criteria);

    int insertByInterface(TdPlanInterface planInterface);
}
