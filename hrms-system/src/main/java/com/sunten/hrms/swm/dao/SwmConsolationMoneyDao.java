package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmConsolationCheck;
import com.sunten.hrms.swm.domain.SwmConsolationMoney;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 慰问金表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2021-08-04
 */
@Mapper
@Repository
public interface SwmConsolationMoneyDao extends BaseMapper<SwmConsolationMoney> {

    int insertAllColumn(SwmConsolationMoney consolationMoney);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmConsolationMoney consolationMoney);

    int updateAllColumnByKey(SwmConsolationMoney consolationMoney);

    SwmConsolationMoney getByKey(@Param(value = "id") Long id);

    List<SwmConsolationMoney> listAllByCriteria(@Param(value = "criteria") SwmConsolationMoneyQueryCriteria criteria);

    List<SwmConsolationMoney> listAllByCriteriaPage(@Param(value = "page") Page<SwmConsolationMoney> page, @Param(value = "criteria") SwmConsolationMoneyQueryCriteria criteria);

    int deleteByEnabled(@Param(value = "id")Long id);

    int updateExportFlagByCriteria(@Param(value = "updateBy")Long id, @Param(value = "criteria") SwmConsolationMoneyQueryCriteria swmConsolationMoneyQueryCriteria);

    int oldestInterfaceToMain(@Param(value = "groupId")Long groupId);

    int releasedInterfaceToMain(@Param(value = "groupId")Long groupId);

    int releasedMoney(SwmConsolationMoney swmConsolationMoney);

    int notReleasedMoney(SwmConsolationMoney swmConsolationMoney);

    Boolean checkHaveBornAfterChildPass(@Param(value = "id")Long id);

    SwmConsolationMoney getSwmConsolationMoneyByOaOrder(@Param(value = "oaOrder")String oaOrder);

    int reBackExport(@Param(value = "updateBy") Long updateBy, @Param(value = "id")Long id);

    int batchReBackExport(@Param(value = "updateBy") Long updateBy, @Param(value = "ids") List<Long> ids);

    int updateDateByChild(@Param(value = "groupId")Long groupId);

    // 自动根据子女幼托生成幼托
    int autoCreateChildByChild();

    // 自动根据子女诞辰生成第一条子女幼托,已存在子女幼托的不生成
    int autoCreateFirstChildByBorn();

    SwmConsolationCheck checkBornBeforeInsert(@Param(value = "employeeId")Long employeeId, @Param(value = "deadDate")LocalDate deadDate, @Param(value = "childName")String childName);

    List<SwmConsolationMoney> listForExportApproval(@Param(value = "ids")List<Long> ids);

    List<SwmConsolationMoney> getListForSendEmail(@Param(value = "ids") Set<Long> ids);
}
