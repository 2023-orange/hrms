package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeePoliticalTemp;
import com.sunten.hrms.pm.dto.PmEmployeePoliticalTempQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 员工政治面貌临时表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@Mapper
@Repository
public interface PmEmployeePoliticalTempDao extends BaseMapper<PmEmployeePoliticalTemp> {

    int insertAllColumn(PmEmployeePoliticalTemp employeePoliticalTemp);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeePoliticalTemp employeePoliticalTemp);

    int updateAllColumnByKey(PmEmployeePoliticalTemp employeePoliticalTemp);

    PmEmployeePoliticalTemp getByKey(@Param(value = "id") Long id);

    List<PmEmployeePoliticalTemp> listAllByCriteria(@Param(value = "criteria") PmEmployeePoliticalTempQueryCriteria criteria);

    List<PmEmployeePoliticalTemp> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeePoliticalTemp> page, @Param(value = "criteria") PmEmployeePoliticalTempQueryCriteria criteria);

    int updateEnableFlag(PmEmployeePoliticalTemp employeePoliticalTemp);

    int updateCheckFlag(PmEmployeePoliticalTemp employeePoliticalTemp);
}
