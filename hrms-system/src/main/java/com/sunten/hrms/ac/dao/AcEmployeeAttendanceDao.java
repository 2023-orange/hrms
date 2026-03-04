package com.sunten.hrms.ac.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.ac.domain.AcCalendarLine;
import com.sunten.hrms.ac.domain.AcEmployeeAttendance;
import com.sunten.hrms.ac.dto.AcEmployeeAttendanceQueryCriteria;
import com.sunten.hrms.ac.vo.NoAttendanceDetailVo;
import com.sunten.hrms.ac.vo.NoAttendanceEmialHeader;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 排班员工考勤制度关系表 Mapper 接口
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Mapper
@Repository
public interface AcEmployeeAttendanceDao extends BaseMapper<AcEmployeeAttendance> {

    int insertAllColumn(AcEmployeeAttendance employeeAttendance);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcEmployeeAttendance employeeAttendance);

    int updateAllColumnByKey(AcEmployeeAttendance employeeAttendance);

    AcEmployeeAttendance getByKey(@Param(value = "id") Long id);

    List<AcEmployeeAttendance> listAllByCriteria(@Param(value = "criteria") AcEmployeeAttendanceQueryCriteria criteria);

    List<AcEmployeeAttendance> listAllByCriteriaPage(@Param(value = "page") Page<AcEmployeeAttendance> page, @Param(value = "criteria") AcEmployeeAttendanceQueryCriteria criteria);

    List<AcCalendarLine> listLineAndAllByCriteria(@Param(value = "criteria") AcEmployeeAttendanceQueryCriteria criteria);

    int updateByEnabled(AcEmployeeAttendance acEmployeeAttendance);

    int disableAttendanceInDatesAndEmpIds(@Param(value = "dates") List<LocalDate> dates,
                                          @Param(value = "employeeIds") List<Long> employeeIds,
                                          @Param(value = "updateBy") Long updateBy,
                                          @Param(value = "updateTime") LocalDateTime updateTime);

    List<AcEmployeeAttendance> listEmployeeAttendanceByCriteria(@Param(value = "criteria") AcEmployeeAttendanceQueryCriteria criteria);

    List<NoAttendanceEmialHeader> getNoAttendanceEmailHeader(@Param(value = "employeeId") Long employeeId);

    List<NoAttendanceDetailVo> getNoAttendanceDetail(List<Long> deptIds);
}
