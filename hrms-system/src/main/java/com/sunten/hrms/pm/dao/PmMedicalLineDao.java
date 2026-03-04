package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmMedicalLine;
import com.sunten.hrms.pm.dto.PmMedicalLineDTO;
import com.sunten.hrms.pm.dto.PmMedicalLineQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.pm.vo.PmMedicalAutoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 体检申请子表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-04-07
 */
@Mapper
@Repository
public interface PmMedicalLineDao extends BaseMapper<PmMedicalLine> {

    int insertAllColumn(PmMedicalLine medicalLine);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByHeaderId(@Param(value = "medicalId") Long medicalId);

    int deleteByEntityKey(PmMedicalLine medicalLine);

    int updateAllColumnByKey(PmMedicalLine medicalLine);

    PmMedicalLine getByKey(@Param(value = "id") Long id);

    List<PmMedicalLine> listAllByCriteria(@Param(value = "criteria") PmMedicalLineQueryCriteria criteria);

    List<PmMedicalLine> listAllByCriteriaPage(@Param(value = "page") Page<PmMedicalLine> page, @Param(value = "criteria") PmMedicalLineQueryCriteria criteria);

    int batchWriteMedicalResult(@Param(value = "criteria") PmMedicalLineQueryCriteria criteria);

    void updateAllColumnFromOa(@Param(value = "id")Long id,
                               @Param(value = "medicalResult")String medicalResult);

    PmMedicalLine getByOA(@Param(value = "employeeName")String employeeName,
                          @Param(value = "medicalClass")String medicalClass);

    List<PmMedicalLineDTO> getMedicalLinesAuto();
}
