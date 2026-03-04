package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeVocationalTemp;
import com.sunten.hrms.pm.dto.PmEmployeeVocationalTempQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 职业资格临时表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeVocationalTempDao extends BaseMapper<PmEmployeeVocationalTemp> {

    int insertAllColumn(PmEmployeeVocationalTemp employeeVocationalTemp);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeVocationalTemp employeeVocationalTemp);

    int updateAllColumnByKey(PmEmployeeVocationalTemp employeeVocationalTemp);

    PmEmployeeVocationalTemp getByKey(@Param(value = "id") Long id);

    List<PmEmployeeVocationalTemp> listAllByCriteria(@Param(value = "criteria") PmEmployeeVocationalTempQueryCriteria criteria);

    List<PmEmployeeVocationalTemp> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeVocationalTemp> page, @Param(value = "criteria") PmEmployeeVocationalTempQueryCriteria criteria);

    int updateEnableFalg(PmEmployeeVocationalTemp employeeVocationalTemp);

    int updateCheckFlag(PmEmployeeVocationalTemp employeeVocationalTemp);
}
