package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeWorkhistoryTemp;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryTempQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 工作经历临时表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@Mapper
@Repository
public interface PmEmployeeWorkhistoryTempDao extends BaseMapper<PmEmployeeWorkhistoryTemp> {

    int insertAllColumn(PmEmployeeWorkhistoryTemp employeeWorkhistoryTemp);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeWorkhistoryTemp employeeWorkhistoryTemp);

    int updateAllColumnByKey(PmEmployeeWorkhistoryTemp employeeWorkhistoryTemp);

    PmEmployeeWorkhistoryTemp getByKey(@Param(value = "id") Long id);

    List<PmEmployeeWorkhistoryTemp> listAllByCriteria(@Param(value = "criteria") PmEmployeeWorkhistoryTempQueryCriteria criteria);

    List<PmEmployeeWorkhistoryTemp> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeWorkhistoryTemp> page, @Param(value = "criteria") PmEmployeeWorkhistoryTempQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeWorkhistoryTemp employeeWorkhistoryTemp);

    int updateCheckFlag(PmEmployeeWorkhistoryTemp employeeWorkhistoryTemp);
}
