package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdPlanImplementDept;
import com.sunten.hrms.td.dto.TdPlanImplementDeptQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训实施参与部门扩展表（用于后期统计使用） Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-06-21
 */
@Mapper
@Repository
public interface TdPlanImplementDeptDao extends BaseMapper<TdPlanImplementDept> {

    int insertAllColumn(TdPlanImplementDept planImplementDept);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdPlanImplementDept planImplementDept);

    int updateAllColumnByKey(TdPlanImplementDept planImplementDept);

    int deleteByEnabled(TdPlanImplementDept planImplementDept);

    TdPlanImplementDept getByKey(@Param(value = "id") Long id);

    List<TdPlanImplementDept> listAllByCriteria(@Param(value = "criteria") TdPlanImplementDeptQueryCriteria criteria);

    List<TdPlanImplementDept> listAllByCriteriaPage(@Param(value = "page") Page<TdPlanImplementDept> page, @Param(value = "criteria") TdPlanImplementDeptQueryCriteria criteria);

    int insertByImplement(TdPlanImplementDept tdPlanImplementDept);
}
