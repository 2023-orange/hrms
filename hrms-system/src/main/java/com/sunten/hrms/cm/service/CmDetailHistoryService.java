package com.sunten.hrms.cm.service;

import com.sunten.hrms.cm.domain.CmDetailHistory;
import com.sunten.hrms.cm.dto.CmDetailHistoryDTO;
import com.sunten.hrms.cm.dto.CmDetailHistoryQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-02-23
 */
public interface CmDetailHistoryService extends IService<CmDetailHistory> {

    CmDetailHistoryDTO insert(CmDetailHistory detailHistoryNew);

    void delete(Long id);

    void delete(CmDetailHistory detailHistory);

    void update(CmDetailHistory detailHistoryNew);

    CmDetailHistoryDTO getByKey(Long id);

    List<CmDetailHistoryDTO> listAll(CmDetailHistoryQueryCriteria criteria);

    Map<String, Object> listAll(CmDetailHistoryQueryCriteria criteria, Pageable pageable);

    void download(List<CmDetailHistoryDTO> detailHistoryDTOS, HttpServletResponse response) throws IOException;

    List<CmDetailHistoryDTO> getByDetailId(Long detailId);

    void insertIntoHistory(Long id);
}
