package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeSocialrelations;
import com.sunten.hrms.pm.dto.PmEmployeeSocialrelationsQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 社会关系表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface PmEmployeeSocialrelationsDao extends BaseMapper<PmEmployeeSocialrelations> {

    int insertAllColumn(PmEmployeeSocialrelations employeeSocialrelations);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeSocialrelations employeeSocialrelations);

    int updateAllColumnByKey(PmEmployeeSocialrelations employeeSocialrelations);

    PmEmployeeSocialrelations getByKey(@Param(value = "id") Long id);

    List<PmEmployeeSocialrelations> listAllByCriteria(@Param(value = "criteria") PmEmployeeSocialrelationsQueryCriteria criteria);

    List<PmEmployeeSocialrelations> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeSocialrelations> page, @Param(value = "criteria") PmEmployeeSocialrelationsQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeSocialrelations employeeSocialrelations);

    List<PmEmployeeSocialrelations> listAllAndTempByEmployee(@Param(value = "employeeId") Long employeeId);

    List<PmEmployeeSocialrelations> superQuery(@Param(value = "queryValue")String queryValue);
}
