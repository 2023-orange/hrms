package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmMedicalProject;
import com.sunten.hrms.pm.dto.PmMedicalProjectQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 体检项目表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-04-19
 */
@Mapper
@Repository
public interface PmMedicalProjectDao extends BaseMapper<PmMedicalProject> {

    int insertAllColumn(PmMedicalProject medicalProject);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmMedicalProject medicalProject);

    int updateAllColumnByKey(PmMedicalProject medicalProject);

    PmMedicalProject getByKey(@Param(value = "id") Long id);

    List<PmMedicalProject> listAllByCriteria(@Param(value = "criteria") PmMedicalProjectQueryCriteria criteria);

    List<PmMedicalProject> listAllByCriteriaPage(@Param(value = "page") Page<PmMedicalProject> page, @Param(value = "criteria") PmMedicalProjectQueryCriteria criteria);
}
