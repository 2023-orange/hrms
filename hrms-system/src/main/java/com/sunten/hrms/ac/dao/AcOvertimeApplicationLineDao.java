package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcOvertimeApplicationLine;
import com.sunten.hrms.ac.dto.AcOvertimeApplicationLineQueryCriteria;
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
 * @since 2023-10-16
 */
@Mapper
@Repository
public interface AcOvertimeApplicationLineDao extends BaseMapper<AcOvertimeApplicationLine> {

    int insertAllColumn(AcOvertimeApplicationLine overtimeApplicationLine);

    int deleteByKey(@Param(value = "id") Integer id);

    int deleteByEntityKey(AcOvertimeApplicationLine overtimeApplicationLine);

    int updateAllColumnByKey(AcOvertimeApplicationLine overtimeApplicationLine);

    AcOvertimeApplicationLine getByKey(@Param(value = "id") Integer id);

    List<AcOvertimeApplicationLine> listAllByCriteria(@Param(value = "criteria") AcOvertimeApplicationLineQueryCriteria criteria);

    List<AcOvertimeApplicationLine> listAllByCriteriaPage(@Param(value = "page") Page<AcOvertimeApplicationLine> page, @Param(value = "criteria") AcOvertimeApplicationLineQueryCriteria criteria);
}
