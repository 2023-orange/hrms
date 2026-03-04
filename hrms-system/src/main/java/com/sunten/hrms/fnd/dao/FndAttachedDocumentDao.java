package com.sunten.hrms.fnd.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.fnd.domain.FndAttachedDocument;
import com.sunten.hrms.fnd.dto.FndAttachedDocumentQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 附件表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-09-25
 */
@Mapper
@Repository
public interface FndAttachedDocumentDao extends BaseMapper<FndAttachedDocument> {

    int insertAllColumn(FndAttachedDocument attachedDocument);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndAttachedDocument attachedDocument);

    int updateAllColumnByKey(FndAttachedDocument attachedDocument);

    FndAttachedDocument getByKey(@Param(value = "id") Long id);

    List<FndAttachedDocument> listAllByCriteria(@Param(value = "criteria") FndAttachedDocumentQueryCriteria criteria);

    List<FndAttachedDocument> listAllByCriteriaPage(@Param(value = "page") Page<FndAttachedDocument> page, @Param(value = "criteria") FndAttachedDocumentQueryCriteria criteria);

    int updateEnabledFlag(FndAttachedDocument attachedDocument);
}
