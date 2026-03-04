package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcAbnormalClockRecord;
import com.sunten.hrms.ac.dto.AcAbnormalClockRecordQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录） Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Mapper
@Repository
public interface AcAbnormalClockRecordDao extends BaseMapper<AcAbnormalClockRecord> {

    int insertAllColumn(AcAbnormalClockRecord abnormalClockRecord);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcAbnormalClockRecord abnormalClockRecord);

    int updateAllColumnByKey(AcAbnormalClockRecord abnormalClockRecord);

    AcAbnormalClockRecord getByKey(@Param(value = "id") Long id);

    List<AcAbnormalClockRecord> listAllByCriteria(@Param(value = "criteria") AcAbnormalClockRecordQueryCriteria criteria);

    List<AcAbnormalClockRecord> listAllByCriteriaPage(@Param(value = "page") Page<AcAbnormalClockRecord> page, @Param(value = "criteria") AcAbnormalClockRecordQueryCriteria criteria);

    int updateDisposeColumnByKey(AcAbnormalClockRecord abnormalClockRecord);
}
