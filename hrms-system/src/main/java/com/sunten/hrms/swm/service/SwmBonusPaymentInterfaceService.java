package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmBonusPaymentInterface;
import com.sunten.hrms.swm.dto.SwmBonusPaymentInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmBonusPaymentInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>
 * 奖金发放接口表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmBonusPaymentInterfaceService extends IService<SwmBonusPaymentInterface> {

    SwmBonusPaymentInterfaceDTO insert(SwmBonusPaymentInterface bonusPaymentInterfaceNew);

    void delete(Long id);

    void delete(SwmBonusPaymentInterface bonusPaymentInterface);

    void update(SwmBonusPaymentInterface bonusPaymentInterfaceNew);

    SwmBonusPaymentInterfaceDTO getByKey(Long id);

    List<SwmBonusPaymentInterfaceDTO> listAll(SwmBonusPaymentInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(SwmBonusPaymentInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<SwmBonusPaymentInterfaceDTO> bonusPaymentInterfaceDTOS, HttpServletResponse response) throws IOException;

    List<SwmBonusPaymentInterfaceDTO> insertExcel(List<SwmBonusPaymentInterface> swmBonusPaymentInterfaces, Boolean reImportFlag);

    void instaceInsert(List<SwmBonusPaymentInterface> swmBonusPaymentInterfaces, Long groupId, Boolean reImportFlag);

    List<SwmBonusPaymentInterface> getBonusPaymentSummaryByImportList(Set<String> workCards, Set<Long> groupIds);
}
