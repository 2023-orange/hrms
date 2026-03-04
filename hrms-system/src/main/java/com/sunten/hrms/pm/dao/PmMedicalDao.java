package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmMedical;
import com.sunten.hrms.pm.dto.PmMedicalQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 体检申请表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-04-07
 */
@Mapper
@Repository
public interface PmMedicalDao extends BaseMapper<PmMedical> {

    int insertAllColumn(PmMedical medical);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmMedical medical);

    int updateAllColumnByKey(PmMedical medical);

    PmMedical getByKey(@Param(value = "id") Long id);

    List<PmMedical> listAllByCriteria(@Param(value = "criteria") PmMedicalQueryCriteria criteria);

    List<PmMedical> listAllByCriteriaPage(@Param(value = "page") Page<PmMedical> page, @Param(value = "criteria") PmMedicalQueryCriteria criteria);
    // 根据申请单号获取体检主表
    PmMedical getByReqCode(@Param(value = "reqCode") String reqCode);

    int updateApprovalColumnByUnderwar(PmMedical medical);

    int updateApprovalColumnByEnd(PmMedical medical);

    int getPmMedicalPass();

    Integer countMedicalApproval(@Param(value = "criteria") PmMedicalQueryCriteria criteria);
}
