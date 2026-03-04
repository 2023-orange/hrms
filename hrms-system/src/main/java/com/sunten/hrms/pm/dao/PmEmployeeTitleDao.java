package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeTitle;
import com.sunten.hrms.pm.dto.PmEmployeeTitleQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 职称情况表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeTitleDao extends BaseMapper<PmEmployeeTitle> {

    int insertAllColumn(PmEmployeeTitle employeeTitle);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeTitle employeeTitle);

    int updateAllColumnByKey(PmEmployeeTitle employeeTitle);

    PmEmployeeTitle getByKey(@Param(value = "id") Long id);

    PmEmployeeTitle getMainTitleByKey(@Param(value = "employeeId") Long employeeId);

    List<PmEmployeeTitle> listAllByCriteria(@Param(value = "criteria") PmEmployeeTitleQueryCriteria criteria);

    List<PmEmployeeTitle> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeTitle> page, @Param(value = "criteria") PmEmployeeTitleQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeTitle employeeTitle);

    int updateNewTitleFlag(PmEmployeeTitle employeeTitle);

    List<PmEmployeeTitle> listAllAndTempByEmployee(@Param(value = "employeeId") Long employeeId);

    List<PmEmployeeTitle> superQuery(@Param(value = "queryValue")String queryValue);
}
