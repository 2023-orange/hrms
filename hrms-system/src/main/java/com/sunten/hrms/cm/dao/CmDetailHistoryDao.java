package com.sunten.hrms.cm.dao;

import com.sunten.hrms.cm.domain.CmDetailHistory;
import com.sunten.hrms.cm.dto.CmDetailHistoryQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujy
 * @since 2023-02-23
 */
@Mapper
@Repository
public interface CmDetailHistoryDao extends BaseMapper<CmDetailHistory> {

    int insertAllColumn(CmDetailHistory detailHistory);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(CmDetailHistory detailHistory);

    int updateAllColumnByKey(CmDetailHistory detailHistory);

    CmDetailHistory getByKey(@Param(value = "id") Long id);

    List<CmDetailHistory> listAllByCriteria(@Param(value = "criteria") CmDetailHistoryQueryCriteria criteria);

    List<CmDetailHistory> listAllByCriteriaPage(@Param(value = "page") Page<CmDetailHistory> page, @Param(value = "criteria") CmDetailHistoryQueryCriteria criteria);

    List<CmDetailHistory> getByDetailId(@Param(value = "detailId") Long detailId);

    int insertIntoHistory(@Param(value = "id") Long id);
}
