package com.sunten.hrms.wta.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.wta.domain.WtaQuartzJob;
import com.sunten.hrms.wta.dto.WtaQuartzJobQueryCriteria;
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
public interface WtaQuartzJobDao extends BaseMapper<WtaQuartzJob> {

    int insertAllColumn(WtaQuartzJob quartzJob);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(WtaQuartzJob quartzJob);

    int updateAllColumnByKey(WtaQuartzJob quartzJob);

    WtaQuartzJob getByKey(@Param(value = "id") Long id);

    List<WtaQuartzJob> listAllByCriteria(@Param(value = "criteria") WtaQuartzJobQueryCriteria criteria);

    List<WtaQuartzJob> listAllByCriteriaPage(@Param(value = "page") Page<WtaQuartzJob> page, @Param(value = "criteria") WtaQuartzJobQueryCriteria criteria);

    List<WtaQuartzJob> listEnableJob();

    List<WtaQuartzJob> listPauseJob();

    int updatePause(WtaQuartzJob quartzJob);
}
