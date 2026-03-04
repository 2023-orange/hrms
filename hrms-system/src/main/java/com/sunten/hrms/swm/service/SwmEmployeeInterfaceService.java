package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmEmployeeInterface;
import com.sunten.hrms.swm.dto.SwmEmployeeInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmEmployeeInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>
 * 薪酬员工信息接口表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-09-13
 */
public interface SwmEmployeeInterfaceService extends IService<SwmEmployeeInterface> {

    SwmEmployeeInterfaceDTO insert(SwmEmployeeInterface employeeInterfaceNew);

    void delete(Double id);

    void delete(SwmEmployeeInterface employeeInterface);

    void update(SwmEmployeeInterface employeeInterfaceNew);

    SwmEmployeeInterfaceDTO getByKey(Double id);

    List<SwmEmployeeInterfaceDTO> listAll(SwmEmployeeInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(SwmEmployeeInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<SwmEmployeeInterfaceDTO> employeeInterfaceDTOS, HttpServletResponse response) throws IOException;

    List<SwmEmployeeInterfaceDTO> importExcel(List<SwmEmployeeInterface> swmEmployeeInterfaces, Boolean reImportFlag);

    void insertMainAndSon(List<SwmEmployeeInterface> swmEmployeeInterfaces, Long groupId, Long userId, Boolean reImportFlag);

    List<SwmEmployeeInterface> getSwmEmployeeSummaryByImportList(Set<String> workCards, Set<Long> groupIds);
}
