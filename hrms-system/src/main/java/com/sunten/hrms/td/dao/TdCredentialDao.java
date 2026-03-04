package com.sunten.hrms.td.dao;

import com.sunten.hrms.td.domain.TdCredential;
import com.sunten.hrms.td.dto.TdCredentialQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 培训证书表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-06-30
 */
@Mapper
@Repository
public interface TdCredentialDao extends BaseMapper<TdCredential> {

    int insertAllColumn(TdCredential credential);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(TdCredential credential);

    int updateAllColumnByKey(TdCredential credential);

    TdCredential getByKey(@Param(value = "id") Long id);

    List<TdCredential> listAllByCriteria(@Param(value = "criteria") TdCredentialQueryCriteria criteria);

    List<TdCredential> listAllByCriteriaPage(@Param(value = "page") Page<TdCredential> page, @Param(value = "criteria") TdCredentialQueryCriteria criteria);
}
