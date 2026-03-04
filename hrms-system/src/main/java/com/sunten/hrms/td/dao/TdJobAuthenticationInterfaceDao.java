package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdJobAuthenticationInterface;
import com.sunten.hrms.td.dto.TdJobAuthenticationInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 上岗认证接口表 Mapper 接口
 * </p>
 *
 * @author xukai
 * @since 2021-10-11
 */
@Mapper
@Repository
public interface TdJobAuthenticationInterfaceDao extends BaseMapper<TdJobAuthenticationInterface> {

    int insertAllColumn(TdJobAuthenticationInterface jobAuthenticationInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdJobAuthenticationInterface jobAuthenticationInterface);

    int updateAllColumnByKey(TdJobAuthenticationInterface jobAuthenticationInterface);

    TdJobAuthenticationInterface getByKey(@Param(value = "id") Long id);

    List<TdJobAuthenticationInterface> listAllByCriteria(@Param(value = "criteria") TdJobAuthenticationInterfaceQueryCriteria criteria);

    List<TdJobAuthenticationInterface> listAllByCriteriaPage(@Param(value = "page") Page<TdJobAuthenticationInterface> page, @Param(value = "criteria") TdJobAuthenticationInterfaceQueryCriteria criteria);

    int insertToInterface(TdJobAuthenticationInterface jobAuthenticationInterface);
}
