package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeePostother;
import com.sunten.hrms.pm.dto.PmEmployeePostotherQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 工作外职务表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeePostotherDao extends BaseMapper<PmEmployeePostother> {

    int insertAllColumn(PmEmployeePostother employeePostother);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeePostother employeePostother);

    int updateAllColumnByKey(PmEmployeePostother employeePostother);

    PmEmployeePostother getByKey(@Param(value = "id") Long id);

    List<PmEmployeePostother> listAllByCriteria(@Param(value = "criteria") PmEmployeePostotherQueryCriteria criteria);

    List<PmEmployeePostother> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeePostother> page, @Param(value = "criteria") PmEmployeePostotherQueryCriteria criteria);

    int updateEnableFlag(PmEmployeePostother employeePostother);

    List<PmEmployeePostother> listAllAndTempByEmployee(@Param(value = "employeeId") Long employeeId);

    List<PmEmployeePostother> superQuery(@Param(value = "queryValue")String queryValue);
}
