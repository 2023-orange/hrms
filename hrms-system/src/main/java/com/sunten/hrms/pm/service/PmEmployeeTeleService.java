package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeTele;
import com.sunten.hrms.pm.dto.PmEmployeeTeleDTO;
import com.sunten.hrms.pm.dto.PmEmployeeTeleQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 办公电话表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeTeleService extends IService<PmEmployeeTele> {

    PmEmployeeTeleDTO insert(PmEmployeeTele employeeTeleNew);

    void delete(Long id);

    void delete(PmEmployeeTele employeeTele);

    void update(PmEmployeeTele employeeTeleNew);

    PmEmployeeTeleDTO getByKey(Long id);

    List<PmEmployeeTeleDTO> listAll(PmEmployeeTeleQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeTeleQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeTeleDTO> employeeTeleDTOS, HttpServletResponse response) throws IOException;

    List<PmEmployeeTeleDTO> batchInsert(List<PmEmployeeTele> teles,Long employeeId);

    void insertRecruitmentTele (PmEmployeeTele tele,Long employeeId);
}
