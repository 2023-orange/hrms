package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeTitle;
import com.sunten.hrms.pm.dto.PmEmployeeTitleDTO;
import com.sunten.hrms.pm.dto.PmEmployeeTitleQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 职称情况表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeTitleService extends IService<PmEmployeeTitle> {

    PmEmployeeTitleDTO insert(PmEmployeeTitle employeeTitleNew);

    void delete(Long id);

    void delete(PmEmployeeTitle employeeTitle);

    void update(PmEmployeeTitle employeeTitleNew);

    PmEmployeeTitleDTO getByKey(Long id);

    List<PmEmployeeTitleDTO> listAll(PmEmployeeTitleQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeTitleQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeTitleDTO> employeeTitleDTOS, HttpServletResponse response) throws IOException;

    List<PmEmployeeTitleDTO> batchInsert(List<PmEmployeeTitle> employeeTitleNews,Long employeeId);

    List<PmEmployeeTitleDTO> listAllByCheck(Long employeeId);
}
