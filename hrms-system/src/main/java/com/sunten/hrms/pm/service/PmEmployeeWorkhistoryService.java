package com.sunten.hrms.pm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.pm.domain.PmEmployeeWorkhistory;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryDTO;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * 工作经历表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeWorkhistoryService extends IService<PmEmployeeWorkhistory> {

    PmEmployeeWorkhistoryDTO insert(PmEmployeeWorkhistory employeeWorkhistoryNew);

    void delete(Long id);

    void delete(PmEmployeeWorkhistory employeeWorkhistory);

    void update(PmEmployeeWorkhistory employeeWorkhistoryNew);

    PmEmployeeWorkhistoryDTO getByKey(Long id);

    List<PmEmployeeWorkhistoryDTO> listAll(PmEmployeeWorkhistoryQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeWorkhistoryQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeWorkhistoryDTO> employeeWorkhistoryDTOS, HttpServletResponse response) throws IOException;

    List<PmEmployeeWorkhistoryDTO> batchInsert(List<PmEmployeeWorkhistory> pmEmployeeWorkhistories,Long employeeId);

    List<PmEmployeeWorkhistoryDTO> listAllByCheck(Long employeeId);
}
