package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReWorkhistory;
import com.sunten.hrms.re.dto.ReWorkhistoryDTO;
import com.sunten.hrms.re.dto.ReWorkhistoryQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 工作经历表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReWorkhistoryService extends IService<ReWorkhistory> {

    ReWorkhistoryDTO insert(ReWorkhistory workhistoryNew);

    void delete(Long id);

    void delete(ReWorkhistory workhistory);

    void update(ReWorkhistory workhistoryNew);

    ReWorkhistoryDTO getByKey(Long id);

    List<ReWorkhistoryDTO> listAll(ReWorkhistoryQueryCriteria criteria);

    Map<String, Object> listAll(ReWorkhistoryQueryCriteria criteria, Pageable pageable);

    void download(List<ReWorkhistoryDTO> workhistoryDTOS, HttpServletResponse response) throws IOException;

    List<ReWorkhistoryDTO> batchInsert(List<ReWorkhistory> reWorkhistories,Long reId);
}
