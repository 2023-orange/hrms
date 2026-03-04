package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmEntryBatchSettingHistory;
import com.sunten.hrms.swm.dto.SwmEntryBatchSettingHistoryQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 人员薪资条目批量设置历史表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmEntryBatchSettingHistoryDao extends BaseMapper<SwmEntryBatchSettingHistory> {

    int insertAllColumn(SwmEntryBatchSettingHistory entryBatchSettingHistory);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmEntryBatchSettingHistory entryBatchSettingHistory);

    int updateAllColumnByKey(SwmEntryBatchSettingHistory entryBatchSettingHistory);

    SwmEntryBatchSettingHistory getByKey(@Param(value = "id") Long id);

    List<SwmEntryBatchSettingHistory> listAllByCriteria(@Param(value = "criteria") SwmEntryBatchSettingHistoryQueryCriteria criteria);

    List<SwmEntryBatchSettingHistory> listAllByCriteriaPage(@Param(value = "page") Page<SwmEntryBatchSettingHistory> page, @Param(value = "criteria") SwmEntryBatchSettingHistoryQueryCriteria criteria);
}
