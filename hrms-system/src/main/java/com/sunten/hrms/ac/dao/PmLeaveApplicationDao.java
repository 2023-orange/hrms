package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.PmLeaveApplication;
import com.sunten.hrms.ac.dto.PmLeaveApplicationQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 请假申请信息表 Mapper 接口
 * </p>
 *
 * @author zouyp
 * @since 2023-06-12
 */
@Mapper
@Repository
public interface PmLeaveApplicationDao extends BaseMapper<PmLeaveApplication> {

    int insertAllColumn(PmLeaveApplication pmLeaveApplication);

    int deleteByKey(@Param(value = "id") Integer id);

    int deleteByEntityKey(PmLeaveApplication pmLeaveApplication);

    int updateAllColumnByKey(PmLeaveApplication pmLeaveApplication);

    PmLeaveApplication getByKey(@Param(value = "id") Integer id);

    List<PmLeaveApplication> listAllByCriteria(@Param(value = "criteria") PmLeaveApplicationQueryCriteria criteria);

    List<PmLeaveApplication> listAllByCriteriaPage(@Param(value = "page") Page<PmLeaveApplication> page, @Param(value = "criteria") PmLeaveApplicationQueryCriteria criteria);
}
