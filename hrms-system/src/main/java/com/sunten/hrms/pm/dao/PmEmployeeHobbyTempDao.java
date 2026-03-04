package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeHobbyTemp;
import com.sunten.hrms.pm.dto.PmEmployeeHobbyTempQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 技术爱好临时表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@Mapper
@Repository
public interface PmEmployeeHobbyTempDao extends BaseMapper<PmEmployeeHobbyTemp> {

    int insertAllColumn(PmEmployeeHobbyTemp employeeHobbyTemp);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeHobbyTemp employeeHobbyTemp);

    int updateAllColumnByKey(PmEmployeeHobbyTemp employeeHobbyTemp);

    PmEmployeeHobbyTemp getByKey(@Param(value = "id") Long id);

    List<PmEmployeeHobbyTemp> listAllByCriteria(@Param(value = "criteria") PmEmployeeHobbyTempQueryCriteria criteria);

    List<PmEmployeeHobbyTemp> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeHobbyTemp> page, @Param(value = "criteria") PmEmployeeHobbyTempQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeHobbyTemp employeeHobbyTemp);

    int updateCheckFlag(PmEmployeeHobbyTemp employeeHobbyTemp);
}
