package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeEducation;
import com.sunten.hrms.pm.dto.PmEmployeeEducationDTO;
import com.sunten.hrms.pm.dto.PmEmployeeEducationQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 教育信息表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeEducationService extends IService<PmEmployeeEducation> {

    PmEmployeeEducationDTO insert(PmEmployeeEducation employeeEducationNew);

    void delete(Long id);

    void delete(PmEmployeeEducation employeeEducation);

    void update(PmEmployeeEducation employeeEducationNew);

    PmEmployeeEducationDTO getByKey(Long id);

    List<PmEmployeeEducationDTO> listAll(PmEmployeeEducationQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeEducationQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeEducationDTO> employeeEducationDTOS, HttpServletResponse response) throws IOException;

    List<PmEmployeeEducationDTO> batchInsert(List<PmEmployeeEducation> employeeEducations,Long employeeId);

    List<PmEmployeeEducationDTO> listAllByCheck(Long employeeId);
}
