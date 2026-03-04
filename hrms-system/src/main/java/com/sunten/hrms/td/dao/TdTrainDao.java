package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdTrain;
import com.sunten.hrms.td.dto.TdTrainQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训情况表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface TdTrainDao extends BaseMapper<TdTrain> {

    int insertAllColumn(TdTrain train);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdTrain train);

    int updateAllColumnByKey(TdTrain train);

    TdTrain getByKey(@Param(value = "id") Long id);

    List<TdTrain> listAllByCriteria(@Param(value = "criteria") TdTrainQueryCriteria criteria);

    List<TdTrain> listAllByCriteriaPage(@Param(value = "page") Page<TdTrain> page, @Param(value = "criteria") TdTrainQueryCriteria criteria);

    int updateEnableFlag(TdTrain train);
}
