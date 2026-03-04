package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcLeaveApplicationLine;
import com.sunten.hrms.ac.dto.AcHrLeaveSubQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zouyp
 * @since 2023-05-30
 */
@Mapper
@Repository
public interface AcLeaveApplicationLineDao extends BaseMapper<AcLeaveApplicationLine> {

    int insertAllColumn(AcLeaveApplicationLine hrLeaveSub);

    int deleteByKey(@Param(value = "id") Integer id);

    int deleteByEntityKey(AcLeaveApplicationLine hrLeaveSub);

    int updateAllColumnByKey(AcLeaveApplicationLine hrLeaveSub);

    AcLeaveApplicationLine getByKey(@Param(value = "id") Integer id);

    List<AcLeaveApplicationLine> listAllByCriteria(@Param(value = "criteria") AcHrLeaveSubQueryCriteria criteria);

    List<AcLeaveApplicationLine> listAllByCriteriaPage(@Param(value = "page") Page<AcLeaveApplicationLine> page, @Param(value = "criteria") AcHrLeaveSubQueryCriteria criteria);

    Double getUsedAnnualLeaveDays(@Param(value = "workCard") String workCard);
}
