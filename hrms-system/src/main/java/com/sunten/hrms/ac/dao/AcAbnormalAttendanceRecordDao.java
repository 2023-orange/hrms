package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcAbnormalAttendanceRecord;
import com.sunten.hrms.ac.dto.AcAbnormalAttendanceRecordQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 异常考勤执行记录 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Mapper
@Repository
public interface AcAbnormalAttendanceRecordDao extends BaseMapper<AcAbnormalAttendanceRecord> {

    int insertAllColumn(AcAbnormalAttendanceRecord abnormalAttendanceRecord);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcAbnormalAttendanceRecord abnormalAttendanceRecord);

    int updateAllColumnByKey(AcAbnormalAttendanceRecord abnormalAttendanceRecord);

    AcAbnormalAttendanceRecord getByKey(@Param(value = "id") Long id);

    List<AcAbnormalAttendanceRecord> listAllByCriteria(@Param(value = "criteria") AcAbnormalAttendanceRecordQueryCriteria criteria);

    List<AcAbnormalAttendanceRecord> listAllByCriteriaPage(@Param(value = "page") Page<AcAbnormalAttendanceRecord> page, @Param(value = "criteria") AcAbnormalAttendanceRecordQueryCriteria criteria);

    AcAbnormalAttendanceRecord getLastOne();
}
