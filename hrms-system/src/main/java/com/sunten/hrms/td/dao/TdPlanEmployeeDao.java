package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdPlanEmployee;
import com.sunten.hrms.td.dto.TdPlanEmployeeQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 参训人员表（包括讲师） Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-05-25
 */
@Mapper
@Repository
public interface TdPlanEmployeeDao extends BaseMapper<TdPlanEmployee> {

    int insertAllColumn(TdPlanEmployee planEmployee);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdPlanEmployee planEmployee);

    int updateAllColumnByKey(TdPlanEmployee planEmployee);

    TdPlanEmployee getByKey(@Param(value = "id") Long id);

    List<TdPlanEmployee> listAllByCriteria(@Param(value = "criteria") TdPlanEmployeeQueryCriteria criteria);

    List<TdPlanEmployee> listAllByCriteriaPage(@Param(value = "page") Page<TdPlanEmployee> page, @Param(value = "criteria") TdPlanEmployeeQueryCriteria criteria);

    int deleteByEnabled(TdPlanEmployee tdPlanEmployee);
}
