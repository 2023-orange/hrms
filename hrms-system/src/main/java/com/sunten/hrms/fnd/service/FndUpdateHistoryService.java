package com.sunten.hrms.fnd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.fnd.domain.FndUpdateHistory;
import com.sunten.hrms.fnd.dto.FndUpdateHistoryDTO;
import com.sunten.hrms.fnd.dto.FndUpdateHistoryQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * 历史修改表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-07-24
 */
public interface FndUpdateHistoryService extends IService<FndUpdateHistory> {

    FndUpdateHistoryDTO insert(FndUpdateHistory updateHistoryNew);

    void delete(Long id);

    void delete(FndUpdateHistory updateHistory);

    void update(FndUpdateHistory updateHistoryNew);

    FndUpdateHistoryDTO getByKey(Long id);

    List<FndUpdateHistoryDTO> listAll(FndUpdateHistoryQueryCriteria criteria);

    Map<String, Object> listAll(FndUpdateHistoryQueryCriteria criteria, Pageable pageable);

    void download(List<FndUpdateHistoryDTO> updateHistoryDTOS, HttpServletResponse response) throws IOException;

    void insertDomainEqualsResultList(String tableName, String columnS, Long id, Object domainNew, Object domainOld);
}
