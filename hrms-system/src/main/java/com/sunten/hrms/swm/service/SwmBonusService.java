package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmBonus;
import com.sunten.hrms.swm.dto.SwmBonusDTO;
import com.sunten.hrms.swm.dto.SwmBonusQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 奖金表	    服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmBonusService extends IService<SwmBonus> {

    SwmBonusDTO insert(SwmBonus bonusNew);

    void delete(Long id);

    void delete(SwmBonus bonus);


    void update(SwmBonus bonusNew);

    SwmBonusDTO getByKey(Long id);

    List<SwmBonusDTO> listAll(SwmBonusQueryCriteria criteria);

    Map<String, Object> listAll(SwmBonusQueryCriteria criteria, Pageable pageable);

    void download(List<SwmBonusDTO> bonusDTOS, HttpServletResponse response) throws IOException;
}
