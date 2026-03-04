package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcCalendarLine;
import com.sunten.hrms.ac.dto.AcCalendarLineQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 日历详细表 Mapper 接口
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Mapper
@Repository
public interface AcCalendarLineDao extends BaseMapper<AcCalendarLine> {

    int insertAllColumn(AcCalendarLine calendarLine);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcCalendarLine calendarLine);

    int updateAllColumnByKey(AcCalendarLine calendarLine);

    int deleteByEnabled(AcCalendarLine calendarLine);

    int insertCollection(List<AcCalendarLine> acCalendarLines);

    AcCalendarLine getByKey(@Param(value = "id") Long id);

    List<AcCalendarLine> listAllByCriteria(@Param(value = "criteria") AcCalendarLineQueryCriteria criteria);

    List<AcCalendarLine> listAllByCriteriaPage(@Param(value = "page") Page<AcCalendarLine> page, @Param(value = "criteria") AcCalendarLineQueryCriteria criteria);

    List<AcCalendarLine> listAllByYear(@Param(value = "year") Integer year, @Param(value = "headerId") Long headerId);

    List<AcCalendarLine> listDefaultCalendarLineByDate(@Param(value = "dateFrom") LocalDate dateFrom, @Param(value = "dateTo") LocalDate dateTo);

    List<AcCalendarLine> listDefaultCalendarLineByDateWithoutDayOff(@Param(value = "dateFrom") LocalDate dateFrom, @Param(value = "dateTo") LocalDate dateTo);

}
