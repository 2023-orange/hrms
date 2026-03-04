package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndSuperQueryTable;
import com.sunten.hrms.fnd.dto.FndSuperQueryTableQueryCriteria;
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
public interface FndSuperQueryTableDao extends BaseMapper<FndSuperQueryTable> {

    int insertAllColumn(FndSuperQueryTable superQueryTable);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndSuperQueryTable superQueryTable);

    int updateAllColumnByKey(FndSuperQueryTable superQueryTable);

    FndSuperQueryTable getByKey(@Param(value = "id") Long id);

    List<FndSuperQueryTable> listAllByCriteria(@Param(value = "criteria") FndSuperQueryTableQueryCriteria criteria);

    List<FndSuperQueryTable> listAllByCriteriaPage(@Param(value = "page") Page<FndSuperQueryTable> page, @Param(value = "criteria") FndSuperQueryTableQueryCriteria criteria);
}
