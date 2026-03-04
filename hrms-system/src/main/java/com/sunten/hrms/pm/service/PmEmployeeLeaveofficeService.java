package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeLeaveoffice;
import com.sunten.hrms.pm.dto.PmEmployeeLeaveofficeDTO;
import com.sunten.hrms.pm.dto.PmEmployeeLeaveofficeQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 离职记录表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeLeaveofficeService extends IService<PmEmployeeLeaveoffice> {

    PmEmployeeLeaveofficeDTO insert(PmEmployeeLeaveoffice employeeLeaveofficeNew);

    void delete(Long id);

    void delete(PmEmployeeLeaveoffice employeeLeaveoffice);

    void update(PmEmployeeLeaveoffice employeeLeaveofficeNew);

    PmEmployeeLeaveofficeDTO getByKey(Long id);

    List<PmEmployeeLeaveofficeDTO> listAll(PmEmployeeLeaveofficeQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeLeaveofficeQueryCriteria criteria, Pageable pageable);

    void download(PmEmployeeLeaveofficeQueryCriteria criteria, Pageable pageable, HttpServletResponse response) throws IOException;
}
