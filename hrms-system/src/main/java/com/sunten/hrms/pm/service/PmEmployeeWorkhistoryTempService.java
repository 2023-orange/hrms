package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeWorkhistoryTemp;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryTempQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 工作经历临时表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
public interface PmEmployeeWorkhistoryTempService extends IService<PmEmployeeWorkhistoryTemp> {

    PmEmployeeWorkhistoryTempDTO insert(PmEmployeeWorkhistoryTemp employeeWorkhistoryTempNew);

    void delete(Long id);

    void delete(PmEmployeeWorkhistoryTemp employeeWorkhistoryTemp);

    void update(PmEmployeeWorkhistoryTemp employeeWorkhistoryTempNew);

    PmEmployeeWorkhistoryTempDTO getByKey(Long id);

    List<PmEmployeeWorkhistoryTempDTO> listAll(PmEmployeeWorkhistoryTempQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeWorkhistoryTempQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeWorkhistoryTempDTO> employeeWorkhistoryTempDTOS, HttpServletResponse response) throws IOException;

    void check(PmEmployeeWorkhistoryTemp employeeWorkhistoryTemp);
}
