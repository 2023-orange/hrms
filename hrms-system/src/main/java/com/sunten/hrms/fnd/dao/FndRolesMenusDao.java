package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndRolesMenus;
import com.sunten.hrms.fnd.dto.FndRolesMenusQueryCriteria;
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
public interface FndRolesMenusDao extends BaseMapper<FndRolesMenus> {

    int insertAllColumn(FndRolesMenus rolesMenus);

    int deleteByKey(@Param(value = "menuId") Long menuId, @Param(value = "roleId") Long roleId);

    int deleteByEntityKey(FndRolesMenus rolesMenus);

    int updateAllColumnByKey(FndRolesMenus rolesMenus);

    FndRolesMenus getByKey(@Param(value = "menuId") Long menuId, @Param(value = "roleId") Long roleId);

    List<FndRolesMenus> listAllByCriteria(@Param(value = "criteria") FndRolesMenusQueryCriteria criteria);

    List<FndRolesMenus> listAllByCriteriaPage(@Param(value = "page") Page<FndRolesMenus> page, @Param(value = "criteria") FndRolesMenusQueryCriteria criteria);

    int untiedMenu(Long id);
}
