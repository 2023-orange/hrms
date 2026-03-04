package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeSocialrelationsTemp;
import com.sunten.hrms.pm.dto.PmEmployeeSocialrelationsTempQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 社会关系临时表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@Mapper
@Repository
public interface PmEmployeeSocialrelationsTempDao extends BaseMapper<PmEmployeeSocialrelationsTemp> {

    int insertAllColumn(PmEmployeeSocialrelationsTemp employeeSocialrelationsTemp);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeSocialrelationsTemp employeeSocialrelationsTemp);

    int updateAllColumnByKey(PmEmployeeSocialrelationsTemp employeeSocialrelationsTemp);

    PmEmployeeSocialrelationsTemp getByKey(@Param(value = "id") Long id);

    List<PmEmployeeSocialrelationsTemp> listAllByCriteria(@Param(value = "criteria") PmEmployeeSocialrelationsTempQueryCriteria criteria);

    List<PmEmployeeSocialrelationsTemp> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeSocialrelationsTemp> page, @Param(value = "criteria") PmEmployeeSocialrelationsTempQueryCriteria criteria);

    int updateEnableFlag(PmEmployeeSocialrelationsTemp employeeSocialrelationsTemp);

    int updateCheckFlag(PmEmployeeSocialrelationsTemp employeeSocialrelationsTemp);
}
