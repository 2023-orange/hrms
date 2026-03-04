package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeEducationTemp;
import com.sunten.hrms.pm.dto.PmEmployeeEducationTempQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 教育信息临时表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeEducationTempDao extends BaseMapper<PmEmployeeEducationTemp> {

    int insertAllColumn(PmEmployeeEducationTemp employeeEducationTemp);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeEducationTemp employeeEducationTemp);

    int updateAllColumnByKey(PmEmployeeEducationTemp employeeEducationTemp);

    PmEmployeeEducationTemp getByKey(@Param(value = "id") Long id);

    List<PmEmployeeEducationTemp> listAllByCriteria(@Param(value = "criteria") PmEmployeeEducationTempQueryCriteria criteria);

    List<PmEmployeeEducationTemp> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeEducationTemp> page, @Param(value = "criteria") PmEmployeeEducationTempQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeEducationTemp employeeEducationTemp);

    int updateCheckFlag(PmEmployeeEducationTemp employeeEducationTemp);
}
