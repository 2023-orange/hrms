package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcRegimeTime;
import com.sunten.hrms.ac.dto.AcRegimeTimeQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 考勤制度时间段表 Mapper 接口
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Mapper
@Repository
public interface AcRegimeTimeDao extends BaseMapper<AcRegimeTime> {

    int insertAllColumn(AcRegimeTime regimeTime);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcRegimeTime regimeTime);

    int updateAllColumnByKey(AcRegimeTime regimeTime);

    int deleteByEnabled(AcRegimeTime acRegimeTime);

    AcRegimeTime getByKey(@Param(value = "id") Long id);

    List<AcRegimeTime> listAllByCriteria(@Param(value = "criteria") AcRegimeTimeQueryCriteria criteria);

    List<AcRegimeTime> listAllByCriteriaPage(@Param(value = "page") Page<AcRegimeTime> page, @Param(value = "criteria") AcRegimeTimeQueryCriteria criteria);
}
