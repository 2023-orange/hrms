package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcCalendarHeader;
import com.sunten.hrms.ac.dto.AcCalendarHeaderQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 日历主表 Mapper 接口
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Mapper
@Repository
public interface AcCalendarHeaderDao extends BaseMapper<AcCalendarHeader> {

    int insertAllColumn(AcCalendarHeader calendarHeader);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcCalendarHeader calendarHeader);

    int deleteByEnabled(AcCalendarHeader calendarHeader);

    int updateAllColumnByKey(AcCalendarHeader calendarHeader);

    AcCalendarHeader getByKey(@Param(value = "id") Long id);

    List<AcCalendarHeader> listAllByCriteria(@Param(value = "criteria") AcCalendarHeaderQueryCriteria criteria);

    List<AcCalendarHeader> listAllByCriteriaPage(@Param(value = "page") Page<AcCalendarHeader> page, @Param(value = "criteria") AcCalendarHeaderQueryCriteria criteria);

    AcCalendarHeader getDefaultHeader();

}
