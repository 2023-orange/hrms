package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmBonus;
import com.sunten.hrms.swm.domain.SwmBonusPayment;
import com.sunten.hrms.swm.dto.SwmBonusPaymentDTO;
import com.sunten.hrms.swm.dto.SwmBonusPaymentQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 奖金发放表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmBonusPaymentService extends IService<SwmBonusPayment> {

    SwmBonusPaymentDTO insert(SwmBonusPayment bonusPaymentNew);

    void delete(Long id);

    void delete(SwmBonusPayment bonusPayment);

    void update(SwmBonusPayment bonusPaymentNew);

    SwmBonusPaymentDTO getByKey(Long id);

    List<SwmBonusPaymentDTO> listAll(SwmBonusPaymentQueryCriteria criteria);

    Map<String, Object> listAll(SwmBonusPaymentQueryCriteria criteria, Pageable pageable);

    void download(List<SwmBonusPaymentDTO> bonusPaymentDTOS, HttpServletResponse response) throws IOException;

    List<SwmBonusPaymentDTO> generateBonusPayment(SwmBonus swmBonus);

    Map<String, Object> selfListAll(SwmBonusPaymentQueryCriteria criteria, Pageable pageable);

    void grantAllBonus(Long bonusId, Boolean grantFlag);
}
