package com.sunten.hrms.ac.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.ac.domain.AcClockRecordRest;
import com.sunten.hrms.ac.domain.AcLeaveApplication;
import com.sunten.hrms.ac.dto.AcClockRecordRestQueryCriteria;
import com.sunten.hrms.ac.dto.AcLeaveApplicationQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zouyp
 */
@Mapper
@Repository
public interface AcClockRecordRestDao extends BaseMapper<AcClockRecordRest> {
    /**
     * 分页查询方法
     * @param page
     * @param criteria
     * @return
     */
    List<AcClockRecordRest> listAllByCriteriaPage(@Param(value = "page") Page<AcClockRecordRest> page, @Param(value = "criteria") AcClockRecordRestQueryCriteria criteria);

    List<AcClockRecordRest> listAll(@Param(value = "criteria") AcClockRecordRestQueryCriteria criteria,@Param("page") int page, @Param("pageSize") int pageSize);

    /**
     * 获取总工有多少条数据
     * @return
     */
    Integer getCount();

    List<AcClockRecordRest> listAll2(@Param(value = "criteria") AcClockRecordRestQueryCriteria criteria, @Param("offset") int offset, @Param("pageSize")  int pageSize);

    Map<String, Object> getInfo(@Param("employeeId") Long employeeId, @Param("date") LocalDate date);

    List<AcClockRecordRest> listAllDownload(AcClockRecordRestQueryCriteria criteria, int pageNumber, int pageSize);
}
