package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeEntry;
import com.sunten.hrms.pm.dto.PmEmployeeEntryQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 入职情况表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeEntryDao extends BaseMapper<PmEmployeeEntry> {

    int insertAllColumn(PmEmployeeEntry employeeEntry);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeEntry employeeEntry);

    int updateAllColumnByKey(PmEmployeeEntry employeeEntry);

    PmEmployeeEntry getByKey(@Param(value = "id") Long id);

    List<PmEmployeeEntry> listAllByCriteria(@Param(value = "criteria") PmEmployeeEntryQueryCriteria criteria);

    List<PmEmployeeEntry> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeEntry> page, @Param(value = "criteria") PmEmployeeEntryQueryCriteria criteria);

    PmEmployeeEntry getByEmployeeId(@Param(value = "employeeId") Long employeeId);

    int updateEnableFlag(PmEmployeeEntry employeeEntry);

    List<PmEmployeeEntry> listAllByProbationCriteriaPage(@Param(value = "page") Page<PmEmployeeEntry> page, @Param(value = "criteria") PmEmployeeEntryQueryCriteria criteria);

    Integer countEntry(@Param(value = "criteria") PmEmployeeEntryQueryCriteria criteria);

    List<PmEmployeeEntry> superQuery(@Param(value = "queryValue")String queryValue);

    int updateAssessFlag(PmEmployeeEntry employeeEntry);
}
