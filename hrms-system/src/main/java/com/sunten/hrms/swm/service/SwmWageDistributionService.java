package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmWageDistribution;
import com.sunten.hrms.swm.dto.SwmWageDistributionDTO;
import com.sunten.hrms.swm.dto.SwmWageDistributionQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 工资分配（工资系数）表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmWageDistributionService extends IService<SwmWageDistribution> {

    SwmWageDistributionDTO insert(SwmWageDistribution wageDistributionNew);

    void delete(Long id);

    void delete(SwmWageDistribution wageDistribution);

    void update(SwmWageDistribution wageDistributionNew);

    SwmWageDistributionDTO getByKey(Long id);

    List<SwmWageDistributionDTO> listAll(SwmWageDistributionQueryCriteria criteria);

    Map<String, Object> listAll(SwmWageDistributionQueryCriteria criteria, Pageable pageable);

    void download(List<SwmWageDistributionDTO> wageDistributionDTOS, HttpServletResponse response) throws IOException;

    List<SwmWageDistribution> generateWageDistributionByMsp(String period);

    void removeByPeriod(String period);

    void batchUpdateWageDistribution(List<SwmWageDistribution> swmWageDistributions);
}
