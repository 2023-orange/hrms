package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmPersonalIncomeTax;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxDTO;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 个人所得税表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmPersonalIncomeTaxService extends IService<SwmPersonalIncomeTax> {

    SwmPersonalIncomeTaxDTO insert(SwmPersonalIncomeTax personalIncomeTaxNew);

    void delete(Long id);

    void delete(SwmPersonalIncomeTax personalIncomeTax);

    void update(SwmPersonalIncomeTax personalIncomeTaxNew);

    SwmPersonalIncomeTaxDTO getByKey(Long id);

    List<SwmPersonalIncomeTaxDTO> listAll(SwmPersonalIncomeTaxQueryCriteria criteria);

    Map<String, Object> listAll(SwmPersonalIncomeTaxQueryCriteria criteria, Pageable pageable);

    void download(List<SwmPersonalIncomeTaxDTO> personalIncomeTaxDTOS, HttpServletResponse response) throws IOException;

    void deleteByIncomePeriod(String incomePeriod, Boolean amountFlag);

    List<SwmPersonalIncomeTaxDTO> getTaxListByUserId();

    List<String> generatePeriodList();
}
