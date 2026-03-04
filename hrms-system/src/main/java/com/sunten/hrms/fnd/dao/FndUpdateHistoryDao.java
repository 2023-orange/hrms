package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndUpdateHistory;
import com.sunten.hrms.fnd.dto.FndUpdateHistoryQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 历史修改表 Mapper 接口
 * </p>
 *
 * @author batan
 * @since 2020-07-24
 */
@Mapper
@Repository
public interface FndUpdateHistoryDao extends BaseMapper<FndUpdateHistory> {

    int insertAllColumn(FndUpdateHistory updateHistory);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndUpdateHistory updateHistory);

    int updateAllColumnByKey(FndUpdateHistory updateHistory);

    FndUpdateHistory getByKey(@Param(value = "id") Long id);

    List<FndUpdateHistory> listAllByCriteria(@Param(value = "criteria") FndUpdateHistoryQueryCriteria criteria);

    List<FndUpdateHistory> listAllByCriteriaPage(@Param(value = "page") Page<FndUpdateHistory> page, @Param(value = "criteria") FndUpdateHistoryQueryCriteria criteria);
}
