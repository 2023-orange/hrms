package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmFloatingWageInterface;
import com.sunten.hrms.swm.dto.SwmFloatingWageInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmFloatingWageInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>
 * 浮动工资接口表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmFloatingWageInterfaceService extends IService<SwmFloatingWageInterface> {

    SwmFloatingWageInterfaceDTO insert(SwmFloatingWageInterface floatingWageInterfaceNew);

    void delete(Long id);

    void delete(SwmFloatingWageInterface floatingWageInterface);

    void update(SwmFloatingWageInterface floatingWageInterfaceNew);

    SwmFloatingWageInterfaceDTO getByKey(Long id);

    List<SwmFloatingWageInterfaceDTO> listAll(SwmFloatingWageInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(SwmFloatingWageInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<SwmFloatingWageInterfaceDTO> floatingWageInterfaceDTOS, HttpServletResponse response) throws IOException;

    List<SwmFloatingWageInterface> insertExcel(List<SwmFloatingWageInterface> swmFloatingWageInterfaces, Boolean reImportFlag);

    void insertMainAndSon(List<SwmFloatingWageInterface> swmFloatingWageInterfaces, Long groupId, Boolean reImportFlag);

    List<SwmFloatingWageInterface> getSummaryByImportList(String incomePeriod, Set<String> workCards, Set<Long> groupIds);
}
