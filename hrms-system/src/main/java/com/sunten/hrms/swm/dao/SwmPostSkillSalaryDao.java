package com.sunten.hrms.swm.dao;

import com.sunten.hrms.swm.domain.SwmPostSkillSalary;
import com.sunten.hrms.swm.domain.SwmPostSkillSalaryCheckMes;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryDTO;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryQueryCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 岗位技能工资表 Mapper 接口
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@Mapper
@Repository
public interface SwmPostSkillSalaryDao extends BaseMapper<SwmPostSkillSalary> {

    int insertAllColumn(SwmPostSkillSalary postSkillSalary);

    int deleteByKey(@Param(value = "id") Long id);

    int deleteByEntityKey(SwmPostSkillSalary postSkillSalary);

    int updateAllColumnByKey(SwmPostSkillSalary postSkillSalary);

    SwmPostSkillSalary getByKey(@Param(value = "id") Long id);

    List<SwmPostSkillSalary> listAllByCriteria(@Param(value = "criteria") SwmPostSkillSalaryQueryCriteria criteria);

    List<SwmPostSkillSalary> listAllByCriteriaPage(@Param(value = "page") Page<SwmPostSkillSalary> page, @Param(value = "criteria") SwmPostSkillSalaryQueryCriteria criteria);

    Integer countByCriteria(@Param(value = "criteria") SwmPostSkillSalaryQueryCriteria criteria);

    int updateByBatchSave(@Param(value = "criteria") SwmPostSkillSalaryQueryCriteria criteria);

    int generatePostSkillSalary(@Param(value = "period") String period);

    // 固定工资生成前的检查
    SwmPostSkillSalaryCheckMes checkBeforePostSkillSalaryGenerate();

    // 用于出右上方period
    String selectTop1Period();

    // 检验冻结
    Integer checkFrozenFlagByPeriod(@Param(value = "incomePeriod")String incomePeriod);

    // 接口转正式
    int interfaceToMain(@Param(value = "groupId")Long groupId);

    int interfaceToMainUpdateWageAndNet(@Param(value = "groupId")Long groupId);

    // 删除固定工资
    int removeByPeriod(SwmPostSkillSalary swmPostSkillSalary);

    void generatePostSkillSalaryByMsp(Map<String,Object> map);

    // 自动计算薪酬扣除
    void autoDeductByMsp(Map<String, Object> map);

    // 发放
    void updateGrantFlagByLimit(@Param(value = "updateLimit") Long updateLimit);

    // 冻结
    void updateFlozenFlagByLimit(@Param(value = "updateLimit") Long updateLimit);

    void updateFlozenFlag(@Param(value = "incomePeriod") String incomePeriod);

    // 根据ids更新实发应发
    void updateWageAndNetAfterUpdate(@Param(value = "ids") List<Long> ids);

    void updateDeductIncomeTaxAfterTaxImport(@Param(value = "incomePeriod") String incomePeriod);

    void updateWageAndNetAfterUpdateCol(@Param(value = "incomePeriod") String incomePeriod);

    // 自动更新前预检
    Boolean checkPostSalaryBeforAutoUpdate(@Param(value = "incomePeriod") String incomePeriod);
}
