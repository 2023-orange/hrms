package com.sunten.hrms.pm.dao;

import com.sunten.hrms.pm.domain.PmEmployeeJobSnapshot;
import com.sunten.hrms.pm.dto.PmEmployeeJobSnapshotQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 部门科室岗位关系快照 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
@Mapper
@Repository
public interface PmEmployeeJobSnapshotDao extends BaseMapper<PmEmployeeJobSnapshot> {

    int insertAllColumn(PmEmployeeJobSnapshot employeeJobSnapshot);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(PmEmployeeJobSnapshot employeeJobSnapshot);

    int updateAllColumnByKey(PmEmployeeJobSnapshot employeeJobSnapshot);

    PmEmployeeJobSnapshot getByKey(@Param(value = "id") Long id);

    List<PmEmployeeJobSnapshot> listAllByCriteria(@Param(value = "criteria") PmEmployeeJobSnapshotQueryCriteria criteria);

    List<PmEmployeeJobSnapshot> listAllByCriteriaPage(@Param(value = "page") Page<PmEmployeeJobSnapshot> page, @Param(value = "criteria") PmEmployeeJobSnapshotQueryCriteria criteria);

    void insertSnapShot(PmEmployeeJobSnapshot pmEmployeeJobSnapshot);

    Integer countByDate();

    List<PmEmployeeJobSnapshot> listAllWithAttendanceByDate(@Param(value = "dateFrom")LocalDate dateFrom, @Param(value = "dateTo")LocalDate dateTO);
}
