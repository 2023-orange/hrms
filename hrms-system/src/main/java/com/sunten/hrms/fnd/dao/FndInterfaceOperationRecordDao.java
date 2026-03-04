package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndInterfaceOperationRecordQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 接口操作记录表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
@Mapper
@Repository
public interface FndInterfaceOperationRecordDao extends BaseMapper<FndInterfaceOperationRecord> {

    int insertAllColumn(FndInterfaceOperationRecord interfaceOperationRecord);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndInterfaceOperationRecord interfaceOperationRecord);

    int updateAllColumnByKey(FndInterfaceOperationRecord interfaceOperationRecord);

    FndInterfaceOperationRecord getByKey(@Param(value = "id") Long id);

    List<FndInterfaceOperationRecord> listAllByCriteria(@Param(value = "criteria") FndInterfaceOperationRecordQueryCriteria criteria);

    List<FndInterfaceOperationRecord> listAllByCriteriaPage(@Param(value = "page") Page<FndInterfaceOperationRecord> page, @Param(value = "criteria") FndInterfaceOperationRecordQueryCriteria criteria);
}
