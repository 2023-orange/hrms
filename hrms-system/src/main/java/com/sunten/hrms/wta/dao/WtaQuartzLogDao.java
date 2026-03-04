package com.sunten.hrms.wta.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.wta.domain.WtaQuartzLog;
import com.sunten.hrms.wta.dto.WtaQuartzLogQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2019-12-23
 */
@Mapper
@Repository
public interface WtaQuartzLogDao extends BaseMapper<WtaQuartzLog> {

    int insertAllColumn(WtaQuartzLog quartzLog);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(WtaQuartzLog quartzLog);

    int updateAllColumnByKey(WtaQuartzLog quartzLog);

    WtaQuartzLog getByKey(@Param(value = "id") Long id);

    List<WtaQuartzLog> listAllByCriteria(@Param(value = "criteria") WtaQuartzLogQueryCriteria criteria);

    List<WtaQuartzLog> listAllByCriteriaPage(@Param(value = "page") Page<WtaQuartzLog> page, @Param(value = "criteria") WtaQuartzLogQueryCriteria criteria);
}
