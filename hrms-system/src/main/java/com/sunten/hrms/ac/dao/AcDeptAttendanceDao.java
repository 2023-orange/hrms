package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcDeptAttendance;
import com.sunten.hrms.ac.dto.AcDeptAttendanceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 部门考勤制度关系表 Mapper 接口
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Mapper
@Repository
public interface AcDeptAttendanceDao extends BaseMapper<AcDeptAttendance> {

    int insertAllColumn(AcDeptAttendance deptAttendance);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcDeptAttendance deptAttendance);

    int deleteByEnabled(AcDeptAttendance deptAttendance);

    int updateAllColumnByKey(AcDeptAttendance deptAttendance);

    AcDeptAttendance getByKey(@Param(value = "id") Long id);

    List<AcDeptAttendance> listAllByCriteria(@Param(value = "criteria") AcDeptAttendanceQueryCriteria criteria);

    List<AcDeptAttendance> listAllByCriteriaPage(@Param(value = "page") Page<AcDeptAttendance> page, @Param(value = "criteria") AcDeptAttendanceQueryCriteria criteria);

    List<AcDeptAttendance> baseListByCriteria(@Param(value = "criteria") AcDeptAttendanceQueryCriteria criteria);

    AcDeptAttendance getByDeptId(@Param(value = "deptId") Long deptId);

    List<AcDeptAttendance> listForDeptHistory(@Param(value = "criteria") AcDeptAttendanceQueryCriteria criteria);

}
