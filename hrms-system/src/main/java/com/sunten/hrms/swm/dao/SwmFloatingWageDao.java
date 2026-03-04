package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmFloatingWage;
import com.sunten.hrms.swm.dto.SwmFloatingWageQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryQueryCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 浮动工资表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmFloatingWageDao extends BaseMapper<SwmFloatingWage> {

    int insertAllColumn(SwmFloatingWage floatingWage);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmFloatingWage floatingWage);

    int updateAllColumnByKey(SwmFloatingWage floatingWage);

    SwmFloatingWage getByKey(@Param(value = "id") Long id);

    List<SwmFloatingWage> listForSecondListByCriteria(@Param(value = "criteria") SwmFloatingWageQueryCriteria criteria);


    List<SwmFloatingWage> listAllByCriteria(@Param(value = "criteria") SwmFloatingWageQueryCriteria criteria);

    List<SwmFloatingWage> listAllByCriteriaPage(@Param(value = "page") Page<SwmFloatingWage> page, @Param(value = "criteria") SwmFloatingWageQueryCriteria criteria);

    void generateFloatingWageByMsp(Map<String, Object> map);

    Integer countByQuery(@Param(value = "criteria") SwmFloatingWageQueryCriteria criteria);

    int removeByPeriod(SwmFloatingWage swmFloatingWage);

    String getTopPeriod();

    int updateFloatWageAfterEdit(@Param(value = "criteria") SwmFloatingWageQueryCriteria criteria);

    void updateGrantFlagByLimit(@Param(value = "updateLimit") Long updateLimit);

    void updateFlozenFlagByLimit(@Param(value = "updateLimit") Long updateLimit);
    // 定时任务调用的接口
    void updateFlozenFlag(@Param(value = "incomePeriod") String incomePeriod);

    String getFloatNewestPeriod();

    void interfaceToMain(@Param(value = "groupId") Long groupId);

    // 检验冻结
    Integer checkFrozenFlagByPeriod(@Param(value = "incomePeriod")String incomePeriod);

    Integer checkIsFlozenWithPeriodList(@Param(value = "periodList")List<String> periodList);

    void updateWageAndNetAfterImportExcel(@Param(value = "groupId") Long groupId);

    Boolean checkFloatingWageBeforAutoUpdate(@Param(value = "incomePeriod") String incomePeriod);

    void updateAllocatePerformancePay(SwmFloatingWage swmFloatingWage);

    Boolean checkFloatingWageIsExist(@Param(value = "incomePeriod")String incomePeriod);

    void updateMonthlyPerformanceSalary(@Param(value = "groupId") Long groupId);
}
