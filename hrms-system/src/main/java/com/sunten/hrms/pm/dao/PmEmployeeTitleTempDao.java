package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeTitleTemp;
import com.sunten.hrms.pm.dto.PmEmployeeTitleTempQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 职称情况临时表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeTitleTempDao extends BaseMapper<PmEmployeeTitleTemp> {

    int insertAllColumn(PmEmployeeTitleTemp employeeTitleTemp);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeTitleTemp employeeTitleTemp);

    int updateAllColumnByKey(PmEmployeeTitleTemp employeeTitleTemp);

    PmEmployeeTitleTemp getByKey(@Param(value = "id") Long id);

    List<PmEmployeeTitleTemp> listAllByCriteria(@Param(value = "criteria") PmEmployeeTitleTempQueryCriteria criteria);

    List<PmEmployeeTitleTemp> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeTitleTemp> page, @Param(value = "criteria") PmEmployeeTitleTempQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeTitleTemp employeeTitleTemp);

    int updateCheckFlag(PmEmployeeTitleTemp employeeTitleTemp);
}
