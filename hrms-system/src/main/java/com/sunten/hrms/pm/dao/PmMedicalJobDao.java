package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmMedicalJob;
import com.sunten.hrms.pm.dto.PmMedicalJobQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 岗位体检表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-04-19
 */
@Mapper
@Repository
public interface PmMedicalJobDao extends BaseMapper<PmMedicalJob> {

    int insertAllColumn(PmMedicalJob medicalJob);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmMedicalJob medicalJob);

    int updateAllColumnByKey(PmMedicalJob medicalJob);

    PmMedicalJob getByKey(@Param(value = "id") Long id);

    List<PmMedicalJob> listAllByCriteria(@Param(value = "criteria") PmMedicalJobQueryCriteria criteria);

    List<PmMedicalJob> listAllByCriteriaPage(@Param(value = "page") Page<PmMedicalJob> page, @Param(value = "criteria") PmMedicalJobQueryCriteria criteria);

    PmMedicalJob getByJobId(@Param(value = "jobId") Long jobId);
}
