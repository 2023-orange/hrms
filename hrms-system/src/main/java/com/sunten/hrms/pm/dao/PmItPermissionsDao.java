package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmItPermissions;
import com.sunten.hrms.pm.dto.PmItPermissionsQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * it权限清单 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-05-10
 */
@Mapper
@Repository
public interface PmItPermissionsDao extends BaseMapper<PmItPermissions> {

    int insertAllColumn(PmItPermissions itPermissions);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmItPermissions itPermissions);

    int updateAllColumnByKey(PmItPermissions itPermissions);

    PmItPermissions getByKey(@Param(value = "id") Long id);

    List<PmItPermissions> listAllByCriteria(@Param(value = "criteria") PmItPermissionsQueryCriteria criteria);

    List<PmItPermissions> listAllByCriteriaPage(@Param(value = "page") Page<PmItPermissions> page, @Param(value = "criteria") PmItPermissionsQueryCriteria criteria);

}
