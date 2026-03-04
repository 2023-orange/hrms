package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeePostotherTemp;
import com.sunten.hrms.pm.dto.PmEmployeePostotherTempQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 工作外职务临时表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@Mapper
@Repository
public interface PmEmployeePostotherTempDao extends BaseMapper<PmEmployeePostotherTemp> {

    int insertAllColumn(PmEmployeePostotherTemp employeePostotherTemp);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeePostotherTemp employeePostotherTemp);

    int updateAllColumnByKey(PmEmployeePostotherTemp employeePostotherTemp);

    PmEmployeePostotherTemp getByKey(@Param(value = "id") Long id);

    List<PmEmployeePostotherTemp> listAllByCriteria(@Param(value = "criteria") PmEmployeePostotherTempQueryCriteria criteria);

    List<PmEmployeePostotherTemp> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeePostotherTemp> page, @Param(value = "criteria") PmEmployeePostotherTempQueryCriteria criteria);

    int updateEnableFlag(PmEmployeePostotherTemp employeePostotherTemp);

    int updateCheckFlag(PmEmployeePostotherTemp employeePostotherTemp);
}
