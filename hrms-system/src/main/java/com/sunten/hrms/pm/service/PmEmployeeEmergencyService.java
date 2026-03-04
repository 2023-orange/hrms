package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeEmergency;
import com.sunten.hrms.pm.dto.PmEmployeeEmergencyDTO;
import com.sunten.hrms.pm.dto.PmEmployeeEmergencyQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 紧急电话表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeEmergencyService extends IService<PmEmployeeEmergency> {

    PmEmployeeEmergencyDTO insert(PmEmployeeEmergency employeeEmergencyNew);

    void delete(Long id);

    void delete(PmEmployeeEmergency employeeEmergency);

    void update(PmEmployeeEmergency employeeEmergencyNew);

    PmEmployeeEmergencyDTO getByKey(Long id);

    List<PmEmployeeEmergencyDTO> listAll(PmEmployeeEmergencyQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeEmergencyQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeEmergencyDTO> employeeEmergencyDTOS, HttpServletResponse response) throws IOException;

    List<PmEmployeeEmergencyDTO> batchInsert(List<PmEmployeeEmergency> employeeEmergencys,Long employeeId);

    void insertRecruitmentEmergency (PmEmployeeEmergency employeeEmergency,Long employeeId);
}
