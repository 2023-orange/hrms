package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndDynamicQueryOperatorGroup;
import com.sunten.hrms.fnd.dto.FndDynamicQueryOperatorGroupQueryCriteria;
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
public interface FndDynamicQueryOperatorGroupDao extends BaseMapper<FndDynamicQueryOperatorGroup> {

    int insertAllColumn(FndDynamicQueryOperatorGroup dynamicQueryOperatorGroup);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndDynamicQueryOperatorGroup dynamicQueryOperatorGroup);

    int updateAllColumnByKey(FndDynamicQueryOperatorGroup dynamicQueryOperatorGroup);

    FndDynamicQueryOperatorGroup getByKey(@Param(value = "id") Long id);

    List<FndDynamicQueryOperatorGroup> listAllByCriteria(@Param(value = "criteria") FndDynamicQueryOperatorGroupQueryCriteria criteria);

    List<FndDynamicQueryOperatorGroup> listAllByCriteriaPage(@Param(value = "page") Page<FndDynamicQueryOperatorGroup> page, @Param(value = "criteria") FndDynamicQueryOperatorGroupQueryCriteria criteria);
}
