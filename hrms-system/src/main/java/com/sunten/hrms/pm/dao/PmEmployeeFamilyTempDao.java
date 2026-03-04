package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeFamilyTemp;
import com.sunten.hrms.pm.dto.PmEmployeeFamilyTempQueryCriteria;
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
 * @author xukai
 * @since 2020-08-25
 */
@Mapper
@Repository
public interface PmEmployeeFamilyTempDao extends BaseMapper<PmEmployeeFamilyTemp> {

    int insertAllColumn(PmEmployeeFamilyTemp employeeFamilyTemp);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeFamilyTemp employeeFamilyTemp);

    int updateAllColumnByKey(PmEmployeeFamilyTemp employeeFamilyTemp);

    PmEmployeeFamilyTemp getByKey(@Param(value = "id") Long id);

    List<PmEmployeeFamilyTemp> listAllByCriteria(@Param(value = "criteria") PmEmployeeFamilyTempQueryCriteria criteria);

    List<PmEmployeeFamilyTemp> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeFamilyTemp> page, @Param(value = "criteria") PmEmployeeFamilyTempQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeFamilyTemp employeeFamilyTemp);

    int updateCheckFlag(PmEmployeeFamilyTemp employeeFamilyTemp);

    int insertColumnToMain(PmEmployeeFamilyTemp employeeFamilyTemp);
}
