package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmPostSkillSalaryInterface;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryInterfaceQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 岗位技能工资接口表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmPostSkillSalaryInterfaceDao extends BaseMapper<SwmPostSkillSalaryInterface> {

    int insertAllColumn(SwmPostSkillSalaryInterface postSkillSalaryInterface);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmPostSkillSalaryInterface postSkillSalaryInterface);

    int updateAllColumnByKey(SwmPostSkillSalaryInterface postSkillSalaryInterface);

    SwmPostSkillSalaryInterface getByKey(@Param(value = "id") Long id);

    List<SwmPostSkillSalaryInterface> listAllByCriteria(@Param(value = "criteria") SwmPostSkillSalaryInterfaceQueryCriteria criteria);

    List<SwmPostSkillSalaryInterface> listAllByCriteriaPage(@Param(value = "page") Page<SwmPostSkillSalaryInterface> page, @Param(value = "criteria") SwmPostSkillSalaryInterfaceQueryCriteria criteria);

    int insertPay(SwmPostSkillSalaryInterface swmPostSkillSalaryInterface);

    int insertPayBySpecial(SwmPostSkillSalaryInterface swmPostSkillSalaryInterface);

    List<SwmPostSkillSalaryInterface> getFixedSummaryByImportList(@Param(value = "incomePeriod")String incomePeriod, @Param(value = "workCards") Set<String> workCards, @Param(value = "groupIds")Set<Long> groupIds);
}
