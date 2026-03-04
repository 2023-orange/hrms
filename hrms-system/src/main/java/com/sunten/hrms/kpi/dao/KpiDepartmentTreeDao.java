package com.sunten.hrms.kpi.dao;

import com.sunten.hrms.kpi.domain.KpiDepartmentTree;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.kpi.vo.KpiDepartmentTreeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * KPI部门树表 Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
@Mapper
@Repository
public interface KpiDepartmentTreeDao extends BaseMapper<KpiDepartmentTree> {

    int insertAllColumn(KpiDepartmentTree departmentTree);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(KpiDepartmentTree departmentTree);

    int updateAllColumnByKey(KpiDepartmentTree departmentTree);

    KpiDepartmentTree getByKey(@Param(value = "id") Long id);

    List<KpiDepartmentTree> listAllByCriteria(@Param(value = "criteria") KpiDepartmentTreeQueryCriteria criteria);

    List<KpiDepartmentTree> listAllByCriteriaPage(@Param(value = "page") Page<KpiDepartmentTree> page, @Param(value = "criteria") KpiDepartmentTreeQueryCriteria criteria);

    int insertTreeFromDept(KpiDepartmentTree departmentTree);

    String getNameByDeptId(Long id, String year);

    KpiDepartmentTree getByDeptId(@Param(value = "id") Long id, @Param(value = "year") String year);

    int updateByKpiTree(KpiDepartmentTree kpiDepartmentTree);
}
