package com.sunten.hrms.pm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.pm.domain.PmEmployeeEmergency;
import com.sunten.hrms.pm.dto.PmEmployeeEmergencyQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 紧急电话表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeEmergencyDao extends BaseMapper<PmEmployeeEmergency> {

    int insertAllColumn(PmEmployeeEmergency employeeEmergency);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeEmergency employeeEmergency);

    int updateAllColumnByKey(PmEmployeeEmergency employeeEmergency);

    PmEmployeeEmergency getByKey(@Param(value = "id") Long id);

    List<PmEmployeeEmergency> listAllByCriteria(@Param(value = "criteria") PmEmployeeEmergencyQueryCriteria criteria);

    List<PmEmployeeEmergency> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeEmergency> page, @Param(value = "criteria") PmEmployeeEmergencyQueryCriteria criteria);

    int updateEnableFlagByKey(PmEmployeeEmergency employeeEmergency);

    List<PmEmployeeEmergency> superQuery(@Param(value = "queryValue")String queryValue);

    int insertByRecruitment(PmEmployeeEmergency employeeEmergency);
}
