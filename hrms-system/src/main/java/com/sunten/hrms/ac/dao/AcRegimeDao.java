package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcRegime;
import com.sunten.hrms.ac.dto.AcRegimeQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 考勤制度表 Mapper 接口
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Mapper
@Repository
public interface AcRegimeDao extends BaseMapper<AcRegime> {

    int insertAllColumn(AcRegime regime);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcRegime regime);

    int updateAllColumnByKey(AcRegime regime);

    int deleteByEnabled(AcRegime regime);

    AcRegime getByKey(@Param(value = "id") Long id);

    List<AcRegime> listAllByCriteria(@Param(value = "criteria") AcRegimeQueryCriteria criteria);

    List<AcRegime> listRelationByCriteria(@Param(value = "criteria") AcRegimeQueryCriteria criteria);

    List<AcRegime> listAllByCriteriaPage(@Param(value = "page") Page<AcRegime> page, @Param(value = "criteria") AcRegimeQueryCriteria criteria);
}
