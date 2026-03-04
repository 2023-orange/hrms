package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmEmployeeInfoInterface;
import com.sunten.hrms.swm.dto.SwmEmployeeInfoInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmEmployeeInfoInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>
 * 薪酬员工基本信息接口表 服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-03-23
 */
public interface SwmEmployeeInfoInterfaceService extends IService<SwmEmployeeInfoInterface> {

    SwmEmployeeInfoInterfaceDTO insert(SwmEmployeeInfoInterface employeeInfoInterfaceNew);

    void delete(Double id);

    void delete(SwmEmployeeInfoInterface employeeInfoInterface);

    void update(SwmEmployeeInfoInterface employeeInfoInterfaceNew);

    SwmEmployeeInfoInterfaceDTO getByKey(Double id);

    List<SwmEmployeeInfoInterfaceDTO> listAll(SwmEmployeeInfoInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(SwmEmployeeInfoInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<SwmEmployeeInfoInterfaceDTO> employeeInfoInterfaceDTOS, HttpServletResponse response) throws IOException;

    List<SwmEmployeeInfoInterfaceDTO> importExcel(List<SwmEmployeeInfoInterface> swmEmployeeInfoInterface);

    List<SwmEmployeeInfoInterface> getSwmEmployeeInfoSummaryByImportList(Set<String> workCards, Set<Long> groupIds);
}
