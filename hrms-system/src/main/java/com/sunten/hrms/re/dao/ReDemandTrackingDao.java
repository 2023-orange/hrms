package com.sunten.hrms.re.dao;

import com.sunten.hrms.re.domain.ReDemandTracking;
import com.sunten.hrms.re.dto.ReDemandTrackingQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 用人需求招聘过程记录 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2022-01-18
 */
@Mapper
@Repository
public interface ReDemandTrackingDao extends BaseMapper<ReDemandTracking> {

    int insertAllColumn(ReDemandTracking demandTracking);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(ReDemandTracking demandTracking);

    int updateAllColumnByKey(ReDemandTracking demandTracking);

    ReDemandTracking getByKey(@Param(value = "id") Long id);

    List<ReDemandTracking> listAllByCriteria(@Param(value = "criteria") ReDemandTrackingQueryCriteria criteria);

    List<ReDemandTracking> listAllByCriteriaPage(@Param(value = "page") Page<ReDemandTracking> page, @Param(value = "criteria") ReDemandTrackingQueryCriteria criteria);

    void deleteByEnabled(@Param(value = "id") Long id, @Param(value = "userId")Long userId);
}
