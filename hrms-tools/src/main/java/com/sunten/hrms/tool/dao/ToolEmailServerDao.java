package com.sunten.hrms.tool.dao;

import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.tool.dto.ToolEmailServerQueryCriteria;
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
 * @since 2020-11-02
 */
@Mapper
@Repository
public interface ToolEmailServerDao extends BaseMapper<ToolEmailServer> {

    int insertAllColumn(ToolEmailServer emailServer);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ToolEmailServer emailServer);

    int updateAllColumnByKey(ToolEmailServer emailServer);

    ToolEmailServer getByKey(@Param(value = "id") Long id);

    List<ToolEmailServer> listAllByCriteria(@Param(value = "criteria") ToolEmailServerQueryCriteria criteria);

    List<ToolEmailServer> listAllByCriteriaPage(@Param(value = "page") Page<ToolEmailServer> page, @Param(value = "criteria") ToolEmailServerQueryCriteria criteria);
}
