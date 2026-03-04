package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndSuperQueryGroup;
import com.sunten.hrms.fnd.dto.FndSuperQueryGroupQueryCriteria;
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
 * @since 2022-08-12
 */
@Mapper
@Repository
public interface FndSuperQueryGroupDao extends BaseMapper<FndSuperQueryGroup> {

    int insertAllColumn(FndSuperQueryGroup superQueryGroup);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndSuperQueryGroup superQueryGroup);

    int updateAllColumnByKey(FndSuperQueryGroup superQueryGroup);

    FndSuperQueryGroup getByKey(@Param(value = "id") Long id);

    List<FndSuperQueryGroup> listAllByCriteriaExpand(@Param(value = "criteria") FndSuperQueryGroupQueryCriteria criteria);

    List<FndSuperQueryGroup> listAllByCriteria(@Param(value = "criteria") FndSuperQueryGroupQueryCriteria criteria);

    List<FndSuperQueryGroup> listAllByCriteriaPage(@Param(value = "page") Page<FndSuperQueryGroup> page, @Param(value = "criteria") FndSuperQueryGroupQueryCriteria criteria);
}
