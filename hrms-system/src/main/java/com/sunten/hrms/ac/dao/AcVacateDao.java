package com.sunten.hrms.ac.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunten.config.DataSourceKeyEnum;
import com.sunten.config.annotation.DataSource;
import com.sunten.hrms.ac.domain.AcVacate;
import com.sunten.hrms.ac.dto.AcVacateQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @atuthor xukai
 * @date 2020/10/15 11:04
 */
@Mapper
@Repository
@DataSource(DataSourceKeyEnum.OA)
public interface AcVacateDao extends BaseMapper<AcVacate> {

    AcVacate getAcVacateByRequisitionCode(@Param(value = "reqCode") String reqCode);

    AcVacate getVacateByReqCodeAndDate(@Param(value = "criteria") AcVacateQueryCriteria criteria);

    List<AcVacate> getList(@Param(value = "criteria") AcVacateQueryCriteria criteria);
}
