package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmCostCenter;
import com.sunten.hrms.swm.dto.SwmCostCenterDTO;
import com.sunten.hrms.swm.dto.SwmCostCenterQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 成本中心表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmCostCenterService extends IService<SwmCostCenter> {

    SwmCostCenterDTO insert(SwmCostCenter costCenterNew);

    void delete(Long id);

    void delete(SwmCostCenter costCenter);

    void update(SwmCostCenter costCenterNew);

    SwmCostCenterDTO getByKey(Long id);

    List<SwmCostCenterDTO> listAll(SwmCostCenterQueryCriteria criteria);

    Map<String, Object> listAll(SwmCostCenterQueryCriteria criteria, Pageable pageable);

    void download(List<SwmCostCenterDTO> costCenterDTOS, HttpServletResponse response) throws IOException;
}
