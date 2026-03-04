package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdPlanChangeHistory;
import com.sunten.hrms.td.dto.TdPlanChangeHistoryDTO;
import com.sunten.hrms.td.dto.TdPlanChangeHistoryQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训计划变更历史 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-06-16
 */
public interface TdPlanChangeHistoryService extends IService<TdPlanChangeHistory> {

    TdPlanChangeHistoryDTO insert(TdPlanChangeHistory planChangeHistoryNew);

    void delete(Long id);

    void delete(TdPlanChangeHistory planChangeHistory);

    void update(TdPlanChangeHistory planChangeHistoryNew);

    TdPlanChangeHistoryDTO getByKey(Long id);

    List<TdPlanChangeHistoryDTO> listAll(TdPlanChangeHistoryQueryCriteria criteria);

    Map<String, Object> listAll(TdPlanChangeHistoryQueryCriteria criteria, Pageable pageable);

    void download(List<TdPlanChangeHistoryDTO> planChangeHistoryDTOS, HttpServletResponse response) throws IOException;

    void updatePassOrNotPass(TdPlanChangeHistory tdPlanChangeHistory);

}
