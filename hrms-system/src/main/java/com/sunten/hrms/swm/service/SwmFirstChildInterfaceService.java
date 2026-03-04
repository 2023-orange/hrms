package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmFirstChildInterface;
import com.sunten.hrms.swm.dto.SwmFirstChildInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmFirstChildInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 第一胎子女信息登记表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-08-10
 */
public interface SwmFirstChildInterfaceService extends IService<SwmFirstChildInterface> {

    SwmFirstChildInterfaceDTO insert(SwmFirstChildInterface firstChildInterfaceNew);

    void delete(Long id);

    void delete(SwmFirstChildInterface firstChildInterface);

    void update(SwmFirstChildInterface firstChildInterfaceNew);

    SwmFirstChildInterfaceDTO getByKey(Long id);

    List<SwmFirstChildInterfaceDTO> listAll(SwmFirstChildInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(SwmFirstChildInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<SwmFirstChildInterfaceDTO> firstChildInterfaceDTOS, HttpServletResponse response) throws IOException;

    List<SwmFirstChildInterfaceDTO> importFirstChildExcel(List<SwmFirstChildInterface> swmFirstChildInterfaces);

    void insertMainAndSon(List<SwmFirstChildInterface> swmFirstChildInterfaces, Long groupId);
}
