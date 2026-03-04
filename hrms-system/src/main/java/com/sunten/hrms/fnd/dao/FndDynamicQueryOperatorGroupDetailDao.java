package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndDynamicQueryOperatorGroupDetail;
import com.sunten.hrms.fnd.dto.FndDynamicQueryOperatorGroupDetailQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * @since 2022-07-26
 */
@Mapper
@Repository
public interface FndDynamicQueryOperatorGroupDetailDao extends BaseMapper<FndDynamicQueryOperatorGroupDetail> {

    int insertAllColumn(FndDynamicQueryOperatorGroupDetail dynamicQueryOperatorGroupDetail);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndDynamicQueryOperatorGroupDetail dynamicQueryOperatorGroupDetail);

    int updateAllColumnByKey(FndDynamicQueryOperatorGroupDetail dynamicQueryOperatorGroupDetail);

    FndDynamicQueryOperatorGroupDetail getByKey(@Param(value = "id") Long id);

    List<FndDynamicQueryOperatorGroupDetail> listAllByCriteria(@Param(value = "criteria") FndDynamicQueryOperatorGroupDetailQueryCriteria criteria);

    List<FndDynamicQueryOperatorGroupDetail> listAllByCriteriaPage(@Param(value = "page") Page<FndDynamicQueryOperatorGroupDetail> page, @Param(value = "criteria") FndDynamicQueryOperatorGroupDetailQueryCriteria criteria);
}
