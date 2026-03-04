package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdPlanEmployeeInterface;
import com.sunten.hrms.td.dto.TdPlanEmployeeInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训参训人员接口表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-09-24
 */
@Mapper
@Repository
public interface TdPlanEmployeeInterfaceDao extends BaseMapper<TdPlanEmployeeInterface> {

    int insertAllColumn(TdPlanEmployeeInterface planEmployeeInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdPlanEmployeeInterface planEmployeeInterface);

    int updateAllColumnByKey(TdPlanEmployeeInterface planEmployeeInterface);

    TdPlanEmployeeInterface getByKey(@Param(value = "id") Long id);

    List<TdPlanEmployeeInterface> listAllByCriteria(@Param(value = "criteria") TdPlanEmployeeInterfaceQueryCriteria criteria);

    List<TdPlanEmployeeInterface> listAllByCriteriaPage(@Param(value = "page") Page<TdPlanEmployeeInterface> page, @Param(value = "criteria") TdPlanEmployeeInterfaceQueryCriteria criteria);

    int insertByInterface(TdPlanEmployeeInterface tdPlanEmployeeInterface);
}
