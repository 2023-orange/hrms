package com.sunten.hrms.pm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.pm.domain.PmEmployeeJobTransfer;
import com.sunten.hrms.pm.dto.PmEmployeeJobTransferQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 岗位调动表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeJobTransferDao extends BaseMapper<PmEmployeeJobTransfer> {

    int insertAllColumn(PmEmployeeJobTransfer employeeJobTransfer);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeJobTransfer employeeJobTransfer);

    int updateAllColumnByKey(PmEmployeeJobTransfer employeeJobTransfer);

    PmEmployeeJobTransfer getByKey(@Param(value = "id") Long id);

    List<PmEmployeeJobTransfer> listAllByCriteria(@Param(value = "criteria") PmEmployeeJobTransferQueryCriteria criteria);

    List<PmEmployeeJobTransfer> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeJobTransfer> page, @Param(value = "criteria") PmEmployeeJobTransferQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeJobTransfer employeeJobTransfer);

    List<PmEmployeeJobTransfer> listFutureTransfer(@Param(value = "employeeId") Long employeeId, @Param(value = "enabledFlag") boolean enabledFlag, @Param(value = "currentDate") LocalDate currentDate);

    List<PmEmployeeJobTransfer> listPlanOrTransferringTransferByEmployeeId(@Param(value = "employeeId") Long employeeId, @Param(value = "groupId") Long groupId);

    Integer countJobTransfer(@Param(value = "criteria") PmEmployeeJobTransferQueryCriteria criteria);

    List<PmEmployeeJobTransfer> superQuery(@Param(value = "queryValue")String queryValue);
}
