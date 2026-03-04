package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.PmLeaveApplicationLine;
import com.sunten.hrms.ac.dto.PmLeaveApplicationLineQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 请假申请信息子表 Mapper 接口
 * </p>
 *
 * @author zouyp
 * @since 2023-06-12
 */
@Mapper
@Repository
public interface PmLeaveApplicationLineDao extends BaseMapper<PmLeaveApplicationLine> {

    int insertAllColumn(PmLeaveApplicationLine pmLeaveApplicationLine);

    int deleteByKey();

    int deleteByEntityKey(PmLeaveApplicationLine pmLeaveApplicationLine);

    int updateAllColumnByKey(PmLeaveApplicationLine pmLeaveApplicationLine);

    PmLeaveApplicationLine getByKey();

    List<PmLeaveApplicationLine> listAllByCriteria(@Param(value = "criteria") PmLeaveApplicationLineQueryCriteria criteria);

    List<PmLeaveApplicationLine> listAllByCriteriaPage(@Param(value = "page") Page<PmLeaveApplicationLine> page, @Param(value = "criteria") PmLeaveApplicationLineQueryCriteria criteria);
}
