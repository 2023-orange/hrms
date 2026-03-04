package com.sunten.hrms.ac.dao;

import com.sunten.hrms.ac.domain.AcLeaveFormTemp;
import com.sunten.hrms.ac.dto.AcLeaveFormTempQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * OA审批通过的请假条临时表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-10-20
 */
@Mapper
@Repository
public interface AcLeaveFormTempDao extends BaseMapper<AcLeaveFormTemp> {

    int insertAllColumn(AcLeaveFormTemp leaveFormTemp);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcLeaveFormTemp leaveFormTemp);

    int updateAllColumnByKey(AcLeaveFormTemp leaveFormTemp);

    AcLeaveFormTemp getByKey(@Param(value = "id") Long id);

    List<AcLeaveFormTemp> listAllByCriteria(@Param(value = "criteria") AcLeaveFormTempQueryCriteria criteria);

    List<AcLeaveFormTemp> listAllByCriteriaPage(@Param(value = "page") Page<AcLeaveFormTemp> page, @Param(value = "criteria") AcLeaveFormTempQueryCriteria criteria);

    List<AcLeaveFormTemp> listAllByMatchEmp();

    int deleteAll();

    int insertCollection(List<AcLeaveFormTemp> acLeaveFormTemps);
}
