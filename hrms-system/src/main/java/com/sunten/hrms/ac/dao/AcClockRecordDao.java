package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcClockRecord;
import com.sunten.hrms.ac.dto.AcClockRecordQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.ac.vo.TempEmployeeClockRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 打卡记录表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Mapper
@Repository
public interface AcClockRecordDao extends BaseMapper<AcClockRecord> {

    int insertAllColumn(AcClockRecord clockRecord);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcClockRecord clockRecord);

    int updateAllColumnByKey(AcClockRecord clockRecord);

    int updateClockForOutRest(AcClockRecord clockRecord);

    AcClockRecord getByKey(@Param(value = "id") Long id);

    List<AcClockRecord> listAllByCriteria(@Param(value = "criteria") AcClockRecordQueryCriteria criteria);

    List<AcClockRecord> listAllByCriteriaPage(@Param(value = "page") Page<AcClockRecord> page, @Param(value = "criteria") AcClockRecordQueryCriteria criteria);

    void updateEmployeeIdMonthly();

    Boolean getFakeRecordSetting(@Param(value = "userName") String userName);

    List<TempEmployeeClockRecordVo> listTempEmployeeClockRecordList(@Param(value= "beginDate")LocalDate beginDate, @Param(value = "endDate")LocalDate endDate);

}
