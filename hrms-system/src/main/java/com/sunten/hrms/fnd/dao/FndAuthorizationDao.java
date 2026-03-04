package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndAuthorization;
import com.sunten.hrms.fnd.dto.FndAuthorizationQueryCriteria;
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
 * @author xukai
 * @since 2021-01-29
 */
@Mapper
@Repository
public interface FndAuthorizationDao extends BaseMapper<FndAuthorization> {

    int insertAllColumn(FndAuthorization authorization);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndAuthorization authorization);

    int updateAllColumnByKey(FndAuthorization authorization);

    FndAuthorization getByKey(@Param(value = "id") Long id);

    List<FndAuthorization> listAllByCriteria(@Param(value = "criteria") FndAuthorizationQueryCriteria criteria);

    List<FndAuthorization> listAllByCriteriaPage(@Param(value = "page") Page<FndAuthorization> page, @Param(value = "criteria") FndAuthorizationQueryCriteria criteria);

    // 失效该授权
    int updateEnableFlagByKey(FndAuthorization authorization);

    List<FndAuthorization> listAllByCriteriaWithChild(@Param(value = "criteria") FndAuthorizationQueryCriteria criteria);
}
