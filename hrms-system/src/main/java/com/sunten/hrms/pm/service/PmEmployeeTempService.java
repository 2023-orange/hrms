package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeTemp;
import com.sunten.hrms.pm.dto.PmEmployeeTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeTempQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 人员临时表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeTempService extends IService<PmEmployeeTemp> {

    PmEmployeeTempDTO insert(PmEmployeeTemp employeeTempNew);

    void delete(Long id);

    void delete(PmEmployeeTemp employeeTemp);

    void update(PmEmployeeTemp employeeTempNew);

    PmEmployeeTempDTO getByKey(Long id);

    List<PmEmployeeTempDTO> listAll(PmEmployeeTempQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeTempQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeTempDTO> employeeTempDTOS, HttpServletResponse response) throws IOException;

    PmEmployeeTempDTO getByEmployeeId(Long employeeId);

    void updateCheckFlag(PmEmployeeTemp employeeTempNew);
}
