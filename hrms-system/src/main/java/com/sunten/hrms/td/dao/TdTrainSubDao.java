package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdTrainSub;
import com.sunten.hrms.td.dto.TdTrainSubQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 参加培训人员情况 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface TdTrainSubDao extends BaseMapper<TdTrainSub> {

    int insertAllColumn(TdTrainSub trainSub);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdTrainSub trainSub);

    int updateAllColumnByKey(TdTrainSub trainSub);

    TdTrainSub getByKey(@Param(value = "id") Long id);

    List<TdTrainSub> listAllByCriteria(@Param(value = "criteria") TdTrainSubQueryCriteria criteria);

    List<TdTrainSub> listAllByCriteriaPage(@Param(value = "page") Page<TdTrainSub> page, @Param(value = "criteria") TdTrainSubQueryCriteria criteria);

    int updateEnableFlag(TdTrainSub trainSub);
}
