package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdPlanResultInterface;
import com.sunten.hrms.td.dto.TdPlanResultInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训结果接口表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-06-17
 */
@Mapper
@Repository
public interface TdPlanResultInterfaceDao extends BaseMapper<TdPlanResultInterface> {

    int insertAllColumn(TdPlanResultInterface planResultInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdPlanResultInterface planResultInterface);

    int updateAllColumnByKey(TdPlanResultInterface planResultInterface);

    TdPlanResultInterface getByKey(@Param(value = "id") Long id);

    List<TdPlanResultInterface> listAllByCriteria(@Param(value = "criteria") TdPlanResultInterfaceQueryCriteria criteria);

    List<TdPlanResultInterface> listAllByCriteriaPage(@Param(value = "page") Page<TdPlanResultInterface> page, @Param(value = "criteria") TdPlanResultInterfaceQueryCriteria criteria);

    int insertByInterface(TdPlanResultInterface planResultInterface);

}
