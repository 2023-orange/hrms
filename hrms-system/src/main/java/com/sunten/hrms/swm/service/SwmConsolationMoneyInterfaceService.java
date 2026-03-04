package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmConsolationMoneyInterface;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 慰问金接口表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-08-05
 */
public interface SwmConsolationMoneyInterfaceService extends IService<SwmConsolationMoneyInterface> {

    SwmConsolationMoneyInterfaceDTO insert(SwmConsolationMoneyInterface consolationMoneyInterfaceNew);

    void delete(Long id);

    void delete(SwmConsolationMoneyInterface consolationMoneyInterface);

    void update(SwmConsolationMoneyInterface consolationMoneyInterfaceNew);

    SwmConsolationMoneyInterfaceDTO getByKey(Long id);

    List<SwmConsolationMoneyInterfaceDTO> listAll(SwmConsolationMoneyInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(SwmConsolationMoneyInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<SwmConsolationMoneyInterfaceDTO> consolationMoneyInterfaceDTOS, HttpServletResponse response) throws IOException;

    void insertMainAndSon(List<SwmConsolationMoneyInterface> swmConsolationMoneyInterfaces, Long groupId, String importType);

    List<SwmConsolationMoneyInterfaceDTO> importExcel(List<SwmConsolationMoneyInterface> swmConsolationMoneyInterfaces, String importType);
}
