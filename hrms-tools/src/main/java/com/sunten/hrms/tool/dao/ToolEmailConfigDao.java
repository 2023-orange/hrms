package com.sunten.hrms.tool.dao;

import com.sunten.hrms.tool.domain.ToolEmailConfig;
import com.sunten.hrms.tool.dto.ToolEmailConfigQueryCriteria;
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
public interface ToolEmailConfigDao extends BaseMapper<ToolEmailConfig> {

    int insertAllColumn(ToolEmailConfig emailConfig);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ToolEmailConfig emailConfig);

    int updateAllColumnByKey(ToolEmailConfig emailConfig);

    ToolEmailConfig getByKey(@Param(value = "id") Long id);

    List<ToolEmailConfig> listAllByCriteria(@Param(value = "criteria") ToolEmailConfigQueryCriteria criteria);

    List<ToolEmailConfig> listAllByCriteriaPage(@Param(value = "page") Page<ToolEmailConfig> page, @Param(value = "criteria") ToolEmailConfigQueryCriteria criteria);
}
