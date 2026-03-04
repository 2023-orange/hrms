package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndRole;
import com.sunten.hrms.fnd.dto.FndRoleQueryCriteria;
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
public interface FndRoleDao extends BaseMapper<FndRole> {

    int insertAllColumn(FndRole role);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndRole role);

    int updateAllColumnByKey(FndRole role);

    FndRole getByKey(@Param(value = "id") Long id);

    List<FndRole> listAllByCriteria(@Param(value = "criteria") FndRoleQueryCriteria criteria);

    List<FndRole> listAllByCriteriaPage(@Param(value = "page") Page<FndRole> page, @Param(value = "criteria") FndRoleQueryCriteria criteria);

    List<FndRole> listAllByPage(@Param(value = "page") Page<FndRole> page);

    FndRole getByName(String name);

    List<FndRole> listByUserId(Long id);
}
