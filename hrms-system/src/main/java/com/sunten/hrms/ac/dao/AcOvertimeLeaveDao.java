package com.sunten.hrms.ac.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.ac.domain.AcOvertimeLeave;
import com.sunten.hrms.ac.domain.AcOvertimeLeaveInterface;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * oa加班请假统计 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
@Mapper
@Repository
public interface AcOvertimeLeaveDao extends BaseMapper<AcOvertimeLeave> {

    int insertAllColumn(AcOvertimeLeave overtimeLeave);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcOvertimeLeave overtimeLeave);

    int updateAllColumnByKey(AcOvertimeLeave overtimeLeave);

    AcOvertimeLeave getByKey(@Param(value = "id") Long id);

    List<AcOvertimeLeave> listAllByCriteria(@Param(value = "criteria") AcOvertimeLeaveQueryCriteria criteria);

    List<AcOvertimeLeave> listAllByCriteriaPage(@Param(value = "page") Page<AcOvertimeLeave> page, @Param(value = "criteria") AcOvertimeLeaveQueryCriteria criteria);

    int insertByInterface(@Param(value = "groupId") Long groupId);

    Integer countDataByMonth(@Param(value = "month") LocalDate month);

    int deleteAllByMonth(@Param(value = "month") LocalDate month);

//    int updateByInterface(AcOvertimeLeaveInterface acOvertimeLeaveInterface);

    void updateByInterface(@Param(value = "groupId") Long groupId);

    void createBeforeinterface(LocalDate date);

    void updateBeforeinterface(LocalDate date);

    int autoCreateLastMonth();

    List<AcOvertimeLeave> sumAcOvertimeLeavePage(@Param(value = "page")Page<AcOvertimeLeave> page, @Param(value = "criteria") AcOvertimeLeaveQueryCriteria acOvertimeLeaveQueryCriteria);

    List<AcOvertimeLeave> sumAcOvertimeLeave(@Param(value = "criteria") AcOvertimeLeaveQueryCriteria acOvertimeLeaveQueryCriteria);

    void updateLastMonthWorkingHours();

    Boolean checkOvertimeEmailIsSendToday(@Param(value = "str") String str);

}
