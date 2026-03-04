package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmPostSkillSalaryInterface;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmPostSkillSalaryInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>
 * 岗位技能工资接口表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmPostSkillSalaryInterfaceService extends IService<SwmPostSkillSalaryInterface> {

    SwmPostSkillSalaryInterfaceDTO insert(SwmPostSkillSalaryInterface postSkillSalaryInterfaceNew);

    void delete(Long id);

    void delete(SwmPostSkillSalaryInterface postSkillSalaryInterface);

    void update(SwmPostSkillSalaryInterface postSkillSalaryInterfaceNew);

    SwmPostSkillSalaryInterfaceDTO getByKey(Long id);

    List<SwmPostSkillSalaryInterfaceDTO> listAll(SwmPostSkillSalaryInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(SwmPostSkillSalaryInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<SwmPostSkillSalaryInterfaceDTO> postSkillSalaryInterfaceDTOS, HttpServletResponse response) throws IOException;

    List<SwmPostSkillSalaryInterface> insertExcel(List<SwmPostSkillSalaryInterface> swmPostSkillSalaryInterfaces, Boolean reImportFlag, String type);

    void insertMainAndSon(List<SwmPostSkillSalaryInterface> swmPostSkillSalaryInterfaces, Long groupId, String importType, Long userId, Boolean specialFlag, Boolean reImportFlag);

    List<SwmPostSkillSalaryInterface> getFixedSummaryByImportList(String incomePeriod, Set<String> workCards, Set<Long> groupIds);

}
