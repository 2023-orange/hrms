package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmPostSkillSalary;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryDTO;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 岗位技能工资表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmPostSkillSalaryService extends IService<SwmPostSkillSalary> {

    SwmPostSkillSalaryDTO insert(SwmPostSkillSalary postSkillSalaryNew);

    void delete(Long id);

    void delete(SwmPostSkillSalary postSkillSalary);
    // 根据周期删除固定
    void deleteByPeriod(String period);

    void update(SwmPostSkillSalary postSkillSalaryNew);

    SwmPostSkillSalaryDTO getByKey(Long id);

    List<SwmPostSkillSalaryDTO> listAll(SwmPostSkillSalaryQueryCriteria criteria);

    Map<String, Object> listAll(SwmPostSkillSalaryQueryCriteria criteria, Pageable pageable);

    void download(List<SwmPostSkillSalaryDTO> postSkillSalaryDTOS, HttpServletResponse response) throws IOException;

    List<SwmPostSkillSalary> generatePostSkillSalary(String period);

    List<String> generatePeriodList();

    void interfaceToMain(Long groupId);

    void interfaceToMainUpdateWageAndNet(Long groupId);

    List<SwmPostSkillSalary> generatePostSkillSalaryByMsp(String period);

    void autoDeductByMsp(String period);

    void setWage(SwmPostSkillSalary postSkillSalaryNew);

    void batchUpdatePostSkillSalary(List<SwmPostSkillSalary> swmPostSkillSalaryList);

    void updateDeductIncomeTaxAfterTaxImport(String incomePeriod);

    void updateWageAndNetAfterUpdateCol(String incomePeriod);

    Boolean checkPostSalaryBeforAutoUpdate(String incomePeriod);

    void sendEmailByOverTimeChange(String period);

    int checkFrozenFlagByPeriod(String period);


}
