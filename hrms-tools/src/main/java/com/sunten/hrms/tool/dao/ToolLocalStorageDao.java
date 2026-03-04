package com.sunten.hrms.tool.dao;

import com.sunten.hrms.tool.domain.ToolLocalStorage;
import com.sunten.hrms.tool.dto.ToolLocalStorageQueryCriteria;
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
 * @since 2019-12-25
 */
@Mapper
@Repository
public interface ToolLocalStorageDao extends BaseMapper<ToolLocalStorage> {

    int insertAllColumn(ToolLocalStorage localStorage);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ToolLocalStorage localStorage);

    int updateAllColumnByKey(ToolLocalStorage localStorage);

    int updateNameById(ToolLocalStorage localStorage);

    ToolLocalStorage getByKey(@Param(value = "id") Long id);

    List<ToolLocalStorage> listAllByCriteria(@Param(value = "criteria") ToolLocalStorageQueryCriteria criteria);

    List<ToolLocalStorage> listAllByCriteriaPage(@Param(value = "page") Page<ToolLocalStorage> page, @Param(value = "criteria") ToolLocalStorageQueryCriteria criteria);
}
