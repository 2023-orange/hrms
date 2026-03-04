package com.sunten.hrms.kpi.service;

import com.sunten.hrms.kpi.domain.KpiAnnual;
import com.sunten.hrms.kpi.dto.KpiAnnualDTO;
import com.sunten.hrms.kpi.dto.KpiAnnualQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * KPI考核年度概况 服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
public interface KpiAnnualService extends IService<KpiAnnual> {

    KpiAnnualDTO insert(KpiAnnual annualNew);

    void delete(Long id);

    void delete(KpiAnnual annual);

    void update(KpiAnnual annualNew);

    KpiAnnualDTO getByKey(Long id);

    List<KpiAnnualDTO> listAll(KpiAnnualQueryCriteria criteria);

    Map<String, Object> listAll(KpiAnnualQueryCriteria criteria, Pageable pageable);

    void download(List<KpiAnnualDTO> annualDTOS, HttpServletResponse response) throws IOException;

    List<Integer> getYearList();
}
