package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReTrain;
import com.sunten.hrms.re.dto.ReTrainQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训记录表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Mapper
@Repository
public interface ReTrainDao extends BaseMapper<ReTrain> {

    int insertAllColumn(ReTrain train);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReTrain train);

    int updateAllColumnByKey(ReTrain train);

    ReTrain getByKey(@Param(value = "id") Long id);

    List<ReTrain> listAllByCriteria(@Param(value = "criteria") ReTrainQueryCriteria criteria);

    List<ReTrain> listAllByCriteriaPage(@Param(value = "page") Page<ReTrain> page, @Param(value = "criteria") ReTrainQueryCriteria criteria);

    int updateEnableFlag(ReTrain train);
}
