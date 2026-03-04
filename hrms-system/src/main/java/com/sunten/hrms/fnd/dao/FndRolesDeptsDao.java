package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndRolesDepts;
import com.sunten.hrms.fnd.dto.FndRolesDeptsQueryCriteria;
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
public interface FndRolesDeptsDao extends BaseMapper<FndRolesDepts> {

    int insertAllColumn(FndRolesDepts rolesDepts);

    int deleteByKey(@Param(value = "roleId") Long roleId, @Param(value = "deptId") Long deptId);

    int deleteByEntityKey(FndRolesDepts rolesDepts);

    int updateAllColumnByKey(FndRolesDepts rolesDepts);

    FndRolesDepts getByKey(@Param(value = "roleId") Long roleId, @Param(value = "deptId") Long deptId);

    List<FndRolesDepts> listAllByCriteria(@Param(value = "criteria") FndRolesDeptsQueryCriteria criteria);

    List<FndRolesDepts> listAllByCriteriaPage(@Param(value = "page") Page<FndRolesDepts> page, @Param(value = "criteria") FndRolesDeptsQueryCriteria criteria);
}
