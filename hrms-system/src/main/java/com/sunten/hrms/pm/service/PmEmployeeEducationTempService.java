package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeEducationTemp;
import com.sunten.hrms.pm.dto.PmEmployeeEducationTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeEducationTempQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 教育信息临时表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeEducationTempService extends IService<PmEmployeeEducationTemp> {

    PmEmployeeEducationTempDTO insert(PmEmployeeEducationTemp employeeEducationTempNew);

    void delete(Long id);

    void delete(PmEmployeeEducationTemp employeeEducationTemp);

    void update(PmEmployeeEducationTemp employeeEducationTempNew);

    PmEmployeeEducationTempDTO getByKey(Long id);

    List<PmEmployeeEducationTempDTO> listAll(PmEmployeeEducationTempQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeEducationTempQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeEducationTempDTO> employeeEducationTempDTOS, HttpServletResponse response) throws IOException;

    void check(PmEmployeeEducationTemp pmEmployeeEducationTemp);
}
