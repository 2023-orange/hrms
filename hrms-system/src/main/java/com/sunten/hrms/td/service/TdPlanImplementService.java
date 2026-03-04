package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdPlanImplement;
import com.sunten.hrms.td.dto.TdPlanImplementDTO;
import com.sunten.hrms.td.dto.TdPlanImplementQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 计划实施申请 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-05-25
 */
public interface TdPlanImplementService extends IService<TdPlanImplement> {

    TdPlanImplementDTO insert(TdPlanImplement planImplementNew);

    void delete(Long id);

    void delete(TdPlanImplement planImplement);

    void update(TdPlanImplement planImplementNew);

    TdPlanImplementDTO getByKey(Long id);

    List<TdPlanImplementDTO> listAll(TdPlanImplementQueryCriteria criteria);

    Map<String, Object> listAll(TdPlanImplementQueryCriteria criteria, Pageable pageable);

    void download(List<TdPlanImplementDTO> planImplementDTOS, HttpServletResponse response) throws IOException;

    void sendEmailWithImplementId(Long implementId);

    TdPlanImplementDTO getByPlanIdForTemplate(Long id);


}
