package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.EmployeeDZB;
import com.sunten.hrms.pm.dto.EmployeeDZBQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-09-09
 */
@Mapper
@Repository
public interface EmployeeDZBDao extends BaseMapper<EmployeeDZB> {

    int insertAllColumn(EmployeeDZB employeeDZB);

    int deleteByKey(@Param(value = "id") Integer id);

    int deleteByEntityKey(EmployeeDZB employeeDZB);

    int updateAllColumnByKey(EmployeeDZB employeeDZB);

    EmployeeDZB getByKey(@Param(value = "id") Integer id);

    List<EmployeeDZB> listAllByCriteria(@Param(value = "criteria") EmployeeDZBQueryCriteria criteria);

    List<EmployeeDZB> listAllByCriteriaPage(@Param(value = "page") Page<EmployeeDZB> page, @Param(value = "criteria") EmployeeDZBQueryCriteria criteria);
}
