package com.sunten.hrms.ac.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.config.DataSourceKeyEnum;
import com.sunten.config.annotation.DataSource;
import com.sunten.hrms.ac.domain.AcOvertime;
import com.sunten.hrms.ac.dto.AcOvertimeQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @atuthor xukai
 * @date 2020/10/16 15:39
 */
@Mapper
@Repository
@DataSource(DataSourceKeyEnum.OA)
public interface AcOvertimeDao extends BaseMapper<AcOvertime> {
    // 查询员工加班信息
    List<AcOvertime> getOvertimeList(@Param(value = "criteria") AcOvertimeQueryCriteria criteria, @Param(value = "page") Page<AcOvertime> page);

}
