package com.sunten.hrms.ac.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.ac.domain.AcOvertimeLeaveInterface;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveInterfaceQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * oa加班请假统计接口表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
@Mapper
@Repository
public interface AcOvertimeLeaveInterfaceDao extends BaseMapper<AcOvertimeLeaveInterface> {

    int insertAllColumn(AcOvertimeLeaveInterface overtimeLeaveInterfe);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(AcOvertimeLeaveInterface overtimeLeaveInterfe);

    int updateAllColumnByKey(AcOvertimeLeaveInterface overtimeLeaveInterfe);

    AcOvertimeLeaveInterface getByKey(@Param(value = "id") Long id);

    List<AcOvertimeLeaveInterface> listAllByCriteria(@Param(value = "criteria") AcOvertimeLeaveInterfaceQueryCriteria criteria);

    List<AcOvertimeLeaveInterface> listAllByCriteriaPage(@Param(value = "page") Page<AcOvertimeLeaveInterface> page, @Param(value = "criteria") AcOvertimeLeaveInterfaceQueryCriteria criteria);

    int checkAndInsertInterFace(AcOvertimeLeaveInterface acOvertimeLeaveInterface);

    Boolean preCheckAcOverTime(AcOvertimeLeaveInterface acOvertimeLeaveInterface);

    void insertOverTimeCollection(@Param(value = "groupId")Long groupId, @Param(value = "list") List<AcOvertimeLeaveInterface> list,
                                  @Param(value = "time")LocalDateTime time, @Param(value= "createBy") Long createBy);

    void checkAfterInsertCollection(@Param(value = "groupId")Long groupId);

    Boolean checkOvertimeLeaveBeforAutoUpdate(@Param(value = "incomePeriod") String incomePeriod);
}
