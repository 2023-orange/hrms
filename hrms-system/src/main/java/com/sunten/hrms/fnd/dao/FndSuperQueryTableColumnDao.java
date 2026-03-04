package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndSuperQueryTableColumn;
import com.sunten.hrms.fnd.dto.FndSuperQueryTableColumnQueryCriteria;
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
public interface FndSuperQueryTableColumnDao extends BaseMapper<FndSuperQueryTableColumn> {

    int insertAllColumn(FndSuperQueryTableColumn superQueryTableColumn);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndSuperQueryTableColumn superQueryTableColumn);

    int updateAllColumnByKey(FndSuperQueryTableColumn superQueryTableColumn);

    FndSuperQueryTableColumn getByKey(@Param(value = "id") Long id);

    List<FndSuperQueryTableColumn> listAllByCriteria(@Param(value = "criteria") FndSuperQueryTableColumnQueryCriteria criteria);

    List<FndSuperQueryTableColumn> listAllByCriteriaPage(@Param(value = "page") Page<FndSuperQueryTableColumn> page, @Param(value = "criteria") FndSuperQueryTableColumnQueryCriteria criteria);
}
