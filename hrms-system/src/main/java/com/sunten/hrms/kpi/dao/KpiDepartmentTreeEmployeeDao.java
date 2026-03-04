package com.sunten.hrms.kpi.dao;

import com.sunten.hrms.kpi.domain.KpiDepartmentTreeEmployee;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeEmployeeQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * KPI资料填写人中间表 Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
@Mapper
@Repository
public interface KpiDepartmentTreeEmployeeDao extends BaseMapper<KpiDepartmentTreeEmployee> {

    int insertAllColumn(KpiDepartmentTreeEmployee departmentTreeEmployee);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(KpiDepartmentTreeEmployee departmentTreeEmployee);

    int updateAllColumnByKey(KpiDepartmentTreeEmployee departmentTreeEmployee);

    KpiDepartmentTreeEmployee getByKey(@Param(value = "id") Long id);

    List<KpiDepartmentTreeEmployee> listAllByCriteria(@Param(value = "criteria") KpiDepartmentTreeEmployeeQueryCriteria criteria);

    List<KpiDepartmentTreeEmployee> listAllByCriteriaPage(@Param(value = "page") Page<KpiDepartmentTreeEmployee> page, @Param(value = "criteria") KpiDepartmentTreeEmployeeQueryCriteria criteria);

    List<KpiDepartmentTreeEmployee> listDepartmentTreeEmployee(@Param(value = "id") Long id);

    int deleteTreeEmployee(@Param(value = "id") Long id);

    List<KpiDepartmentTreeEmployee> getKpiEmployee(KpiDepartmentTreeEmployeeQueryCriteria criteria);
}
