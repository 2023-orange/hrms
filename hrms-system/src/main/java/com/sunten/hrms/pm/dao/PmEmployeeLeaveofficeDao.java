package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeLeaveoffice;
import com.sunten.hrms.pm.dto.PmEmployeeLeaveofficeQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 离职记录表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeLeaveofficeDao extends BaseMapper<PmEmployeeLeaveoffice> {

    int insertAllColumn(PmEmployeeLeaveoffice employeeLeaveoffice);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeLeaveoffice employeeLeaveoffice);

    int updateAllColumnByKey(PmEmployeeLeaveoffice employeeLeaveoffice);

    PmEmployeeLeaveoffice getByKey(@Param(value = "id") Long id);

    List<PmEmployeeLeaveoffice> listAllByCriteria(@Param(value = "criteria") PmEmployeeLeaveofficeQueryCriteria criteria);

    List<PmEmployeeLeaveoffice> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeLeaveoffice> page, @Param(value = "criteria") PmEmployeeLeaveofficeQueryCriteria criteria);

    PmEmployeeLeaveoffice getByEmployeeId(@Param(value = "employeeId") Long employeeId);

    int updateEnableFlag(PmEmployeeLeaveoffice employeeLeaveoffice);

    List<PmEmployeeLeaveoffice> superQuery(@Param(value = "queryValue")String queryValue);
}
