package com.sunten.hrms.fnd.dao;

import com.sunten.hrms.fnd.domain.FndLog;
import com.sunten.hrms.fnd.dto.FndLogQueryCriteria;
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
 * @author batan
 * @since 2019-12-23
 */
@Mapper
@Repository
public interface FndLogDao extends BaseMapper<FndLog> {

    int insertAllColumn(FndLog log);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(FndLog log);

    int updateAllColumnByKey(FndLog log);

    FndLog getByKey(@Param(value = "id") Long id);

    List<FndLog> listAllByCriteria(@Param(value = "criteria") FndLogQueryCriteria criteria);

    List<FndLog> listAllByCriteriaPage(@Param(value = "page") Page<FndLog> page, @Param(value = "criteria") FndLogQueryCriteria criteria);

    Long findIp(String date1, String date2);
}
