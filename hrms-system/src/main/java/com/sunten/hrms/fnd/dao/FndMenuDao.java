package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndMenu;
import com.sunten.hrms.fnd.dto.FndMenuQueryCriteria;
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
 * @since 2019-12-19
 */
@Mapper
@Repository
public interface FndMenuDao extends BaseMapper<FndMenu> {

    int insertAllColumn(FndMenu menu);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndMenu menu);

    int updateAllColumnByKey(FndMenu menu);

    FndMenu getByKey(@Param(value = "id") Long id);

    List<FndMenu> listAllByCriteria(@Param(value = "criteria") FndMenuQueryCriteria criteria);

    List<FndMenu> listAllByCriteriaPage(@Param(value = "page") Page<FndMenu> page, @Param(value = "criteria") FndMenuQueryCriteria criteria);

    FndMenu getByName(@Param(value = "name") String name);

    FndMenu getByComponentName(@Param(value = "name") String name);

    List<FndMenu> listByPid(@Param(value = "pid") long pid);

    List<FndMenu> listByRolesIdExType(@Param(value = "roleId") Long roleId, @Param(value = "typeId") Integer typeId);

    List<FndMenu> listByRolesIdsAndType(@Param(value = "roleIds") List<Long> roleIds, @Param(value = "typeId") Integer typeId);
}
