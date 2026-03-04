package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmEntryBatchSettingHistory;
import com.sunten.hrms.swm.dto.SwmEntryBatchSettingHistoryDTO;
import com.sunten.hrms.swm.dto.SwmEntryBatchSettingHistoryQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.swm.vo.BatchSettingVo;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 人员薪资条目批量设置历史表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmEntryBatchSettingHistoryService extends IService<SwmEntryBatchSettingHistory> {

    SwmEntryBatchSettingHistoryDTO insert(SwmEntryBatchSettingHistory entryBatchSettingHistoryNew);

    void delete(Long id);

    void delete(SwmEntryBatchSettingHistory entryBatchSettingHistory);

    void update(SwmEntryBatchSettingHistory entryBatchSettingHistoryNew);

    SwmEntryBatchSettingHistoryDTO getByKey(Long id);

    List<BatchSettingVo> listAll(SwmEntryBatchSettingHistoryQueryCriteria criteria);

    Map<String, Object> listAll(SwmEntryBatchSettingHistoryQueryCriteria criteria, Pageable pageable);

    void download(List<BatchSettingVo> entryBatchSettingHistoryDTOS, HttpServletResponse response) throws IOException;
}
