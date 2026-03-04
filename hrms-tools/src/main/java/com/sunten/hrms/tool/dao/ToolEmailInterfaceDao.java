package com.sunten.hrms.tool.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.dto.ToolEmailInterfaceQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-10-30
 */
@Mapper
@Repository
public interface ToolEmailInterfaceDao extends BaseMapper<ToolEmailInterface> {

    int insertAllColumn(ToolEmailInterface emailInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ToolEmailInterface emailInterface);

    int updateAllColumnByKey(ToolEmailInterface emailInterface);

    int updateStatus(ToolEmailInterface emailInterface);

    ToolEmailInterface getByKey(@Param(value = "id") Long id);

    List<ToolEmailInterface> listAllByCriteria(@Param(value = "criteria") ToolEmailInterfaceQueryCriteria criteria);

    List<ToolEmailInterface> listAllByCriteriaPage(@Param(value = "page") Page<ToolEmailInterface> page, @Param(value = "criteria") ToolEmailInterfaceQueryCriteria criteria);
}
