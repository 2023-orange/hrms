package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmEmployeeHistory;
import com.sunten.hrms.swm.dto.SwmEmployeeHistoryDTO;
import com.sunten.hrms.swm.dto.SwmEmployeeHistoryQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 员工信息历史表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmEmployeeHistoryService extends IService<SwmEmployeeHistory> {

    SwmEmployeeHistoryDTO insert(SwmEmployeeHistory employeeHistoryNew);

    void delete(Long id);

    void delete(SwmEmployeeHistory employeeHistory);

    void update(SwmEmployeeHistory employeeHistoryNew);

    SwmEmployeeHistoryDTO getByKey(Long id);

    List<SwmEmployeeHistoryDTO> listAll(SwmEmployeeHistoryQueryCriteria criteria);

    Map<String, Object> listAll(SwmEmployeeHistoryQueryCriteria criteria, Pageable pageable);

    void download(List<SwmEmployeeHistoryDTO> employeeHistoryDTOS, HttpServletResponse response) throws IOException;

    List<SwmEmployeeHistoryDTO> getSwmEmployeeHistoryByWorkCard(String workCard);
}
