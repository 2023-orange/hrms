package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmWageSummaryFile;
import com.sunten.hrms.swm.dto.SwmWageSummaryFileDTO;
import com.sunten.hrms.swm.dto.SwmWageSummaryFileQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 工资汇总归档表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-12-25
 */
public interface SwmWageSummaryFileService extends IService<SwmWageSummaryFile> {

    SwmWageSummaryFileDTO insert(SwmWageSummaryFile wageSummaryFileNew);

    void delete(Long id);

    void delete(SwmWageSummaryFile wageSummaryFile);

    void update(SwmWageSummaryFile wageSummaryFileNew);

    SwmWageSummaryFileDTO getByKey(Long id);

    List<SwmWageSummaryFileDTO> listAll(SwmWageSummaryFileQueryCriteria criteria);

    Map<String, Object> listAll(SwmWageSummaryFileQueryCriteria criteria, Pageable pageable);

    void download(List<SwmWageSummaryFileDTO> wageSummaryFileDTOS, HttpServletResponse response) throws IOException;

    void autoInsertFile();

    void frozenSalary(String type, Long limit, String period);

    void grantSalary(String type, Long limit, String period);

    void exportForTaxCalculation(List<SwmWageSummaryFileDTO> wageSummaryFileDTOS, HttpServletResponse response) throws  IOException;

    void exportForCostCenter(HttpServletResponse response, SwmWageSummaryFileQueryCriteria criteria) throws  IOException;

    // 返回员工的薪资历史
    List<SwmWageSummaryFileDTO> getGrantDetail(Long employeeId, String year);

    List<SwmWageSummaryFileDTO> listStatistics(String workCard);

    Boolean checkFrozenFlagByPeriod(String period);
}
