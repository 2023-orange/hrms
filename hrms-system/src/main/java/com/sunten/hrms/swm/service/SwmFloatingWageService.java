package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmFloatingWage;
import com.sunten.hrms.swm.dto.SwmFloatingWageDTO;
import com.sunten.hrms.swm.dto.SwmFloatingWageQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.swm.dto.SwmFloatingWageSpecialDTO;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 浮动工资表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmFloatingWageService extends IService<SwmFloatingWage> {

    SwmFloatingWageDTO insert(SwmFloatingWage floatingWageNew);

    void delete(Long id);

    void delete(SwmFloatingWage floatingWage);

    void update(SwmFloatingWage floatingWageNew);

    SwmFloatingWageDTO getByKey(Long id);

    List<SwmFloatingWageDTO> listAll(SwmFloatingWageQueryCriteria criteria);

    List<SwmFloatingWageSpecialDTO> listSpecialAll(SwmFloatingWageQueryCriteria criteria);

    Map<String, Object> listAll(SwmFloatingWageQueryCriteria criteria, Pageable pageable);

    void download(List<SwmFloatingWageDTO> floatingWageDTOS, HttpServletResponse response) throws IOException;

    void downloadSecondList(List<SwmFloatingWageSpecialDTO> floatingWageDTOS, HttpServletResponse response) throws IOException;

    List<SwmFloatingWageDTO> generateFloatingWageByMsp(String period);

    // 根据周期删除
    void deleteByPeriod(String period);

    List<String> generatePeriodList();

    // 获取最新未冻结的
    String getFloatNewestPeriod();

    void batchUpdateFloatingWage(List<SwmFloatingWage> swmFloatingWages);

    Boolean checkFloatingWageBeforAutoUpdate(String incomePeriod);

    void batchUpdateAllocatePerformancePay(List<SwmFloatingWage> swmFloatingWages);

    Boolean checkFloatingWageIsExist(String incomePeriod);

    int checkFrozenFlagByPeriod(String period);
}
