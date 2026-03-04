package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeContract;
import com.sunten.hrms.pm.dto.PmEmployeeContractDTO;
import com.sunten.hrms.pm.dto.PmEmployeeContractQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 合同情况表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeContractDao extends BaseMapper<PmEmployeeContract> {

    int insertAllColumn(PmEmployeeContract employeeContract);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeContract employeeContract);

    int updateAllColumnByKey(PmEmployeeContract employeeContract);

    PmEmployeeContract getByKey(@Param(value = "id") Long id);

    List<PmEmployeeContract> listAllByCriteria(@Param(value = "criteria") PmEmployeeContractQueryCriteria criteria);

    List<PmEmployeeContract> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeContract> page, @Param(value = "criteria") PmEmployeeContractQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeContract employeeContract);

    int updateNewContractFlag(PmEmployeeContract employeeContract);

    PmEmployeeContract getNewContractByEmployee(@Param(value = "employeeId") Long employeeId);

    Integer countContract(@Param(value = "criteria") PmEmployeeContractQueryCriteria criteria);

    List<PmEmployeeContract> listAllAndSignNumByCriteriaPage(@Param(value = "page") Page<PmEmployeeContract> page, @Param(value = "criteria") PmEmployeeContractQueryCriteria criteria);

    List<PmEmployeeContract> listAllAndSignNumByCriteria(@Param(value = "criteria") PmEmployeeContractQueryCriteria criteria);

    List<PmEmployeeContract> superQuery(@Param(value = "queryValue")String queryValue);

    List<PmEmployeeContract> getAllByemployeeId(@Param(value = "employeeId")Long employeeId);

    PmEmployeeContract getNewestContractByEmployeeId(@Param(value = "employeeId") Long employeeId);
}
