package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeTemp;
import com.sunten.hrms.pm.dto.PmEmployeeTempQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 人员临时表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeTempDao extends BaseMapper<PmEmployeeTemp> {

    int insertAllColumn(PmEmployeeTemp employeeTemp);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeTemp employeeTemp);

    int updateAllColumnByKey(PmEmployeeTemp employeeTemp);

    PmEmployeeTemp getByKey(@Param(value = "id") Long id);

    List<PmEmployeeTemp> listAllByCriteria(@Param(value = "criteria") PmEmployeeTempQueryCriteria criteria);

    List<PmEmployeeTemp> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeTemp> page, @Param(value = "criteria") PmEmployeeTempQueryCriteria criteria);

    PmEmployeeTemp getByEmployeeId(@Param(value = "employeeId") Long employeeId);

    int updateEnableFlag(PmEmployeeTemp employeeTemp);

    int updateCheckFlag(PmEmployeeTemp employeeTemp);
}
