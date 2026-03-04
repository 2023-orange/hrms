package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmPersonalIncomeTaxInterface;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 个人所得税接口表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-01-14
 */
public interface SwmPersonalIncomeTaxInterfaceService extends IService<SwmPersonalIncomeTaxInterface> {

    SwmPersonalIncomeTaxInterfaceDTO insert(SwmPersonalIncomeTaxInterface personalIncomeTaxInterfaceNew);

    void delete(Long id);

    void delete(SwmPersonalIncomeTaxInterface personalIncomeTaxInterface);

    void update(SwmPersonalIncomeTaxInterface personalIncomeTaxInterfaceNew);

    SwmPersonalIncomeTaxInterfaceDTO getByKey(Long id);

    List<SwmPersonalIncomeTaxInterfaceDTO> listAll(SwmPersonalIncomeTaxInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(SwmPersonalIncomeTaxInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<SwmPersonalIncomeTaxInterfaceDTO> personalIncomeTaxInterfaceDTOS, HttpServletResponse response) throws IOException;

    List<SwmPersonalIncomeTaxInterfaceDTO> insertExcel(List<SwmPersonalIncomeTaxInterface> swmPersonalIncomeTaxInterfaces, Boolean amountFlag, Boolean reImportFlag);

    void insertMainAndSon(List<SwmPersonalIncomeTaxInterface> swmPersonalIncomeTaxInterfaces, Long groupId, Long userId,
                          String incomePeriod, String taxPeriod, Boolean amountFlag, Boolean reImportFlag);

    Boolean checkTax(List<SwmPersonalIncomeTaxInterface> swmPersonalIncomeTaxInterfaces);

    Boolean checkTaxWithNotAmount(List<SwmPersonalIncomeTaxInterface> swmPersonalIncomeTaxInterfaces);

}
