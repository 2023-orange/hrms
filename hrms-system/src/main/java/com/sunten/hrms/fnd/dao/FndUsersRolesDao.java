package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndUsersRoles;
import com.sunten.hrms.fnd.dto.FndUsersRolesQueryCriteria;
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
public interface FndUsersRolesDao extends BaseMapper<FndUsersRoles> {

    int insertAllColumn(FndUsersRoles usersRoles);

    int deleteByKey(@Param(value = "userId") Long userId, @Param(value = "roleId") Long roleId);

    int deleteByEntityKey(FndUsersRoles usersRoles);

    int updateAllColumnByKey(FndUsersRoles usersRoles);

    FndUsersRoles getByKey(@Param(value = "userId") Long userId, @Param(value = "roleId") Long roleId);

    List<FndUsersRoles> listAllByCriteria(@Param(value = "criteria") FndUsersRolesQueryCriteria criteria);

    List<FndUsersRoles> listAllByCriteriaPage(@Param(value = "page") Page<FndUsersRoles> page, @Param(value = "criteria") FndUsersRolesQueryCriteria criteria);

    Boolean checkHaveRoleBySeIdAndPermission(@Param(value = "seId")Long seId, @Param(value = "permission") String permission);

    Boolean checkHaveRoleByUserIdAndPermission(@Param(value = "userId")Long userId, @Param(value = "permission")String permission);
}
