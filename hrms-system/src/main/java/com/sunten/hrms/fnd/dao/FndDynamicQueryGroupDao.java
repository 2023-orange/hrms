package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndDynamicQueryGroup;
import com.sunten.hrms.fnd.dto.FndDynamicQueryGroupQueryCriteria;
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
public interface FndDynamicQueryGroupDao extends BaseMapper<FndDynamicQueryGroup> {

    int insertAllColumn(FndDynamicQueryGroup dynamicQueryGroup);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndDynamicQueryGroup dynamicQueryGroup);

    int updateAllColumnByKey(FndDynamicQueryGroup dynamicQueryGroup);

    FndDynamicQueryGroup getByKey(@Param(value = "id") Long id);

    List<FndDynamicQueryGroup> listAllByCriteria(@Param(value = "criteria") FndDynamicQueryGroupQueryCriteria criteria);

    List<FndDynamicQueryGroup> listAllByCriteriaPage(@Param(value = "page") Page<FndDynamicQueryGroup> page, @Param(value = "criteria") FndDynamicQueryGroupQueryCriteria criteria);
}
