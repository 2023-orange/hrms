package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeHobbyTemp;
import com.sunten.hrms.pm.dto.PmEmployeeHobbyTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeHobbyTempQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 技术爱好临时表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
public interface PmEmployeeHobbyTempService extends IService<PmEmployeeHobbyTemp> {

    PmEmployeeHobbyTempDTO insert(PmEmployeeHobbyTemp employeeHobbyTempNew);

    void delete(Long id);

    void delete(PmEmployeeHobbyTemp employeeHobbyTemp);

    void update(PmEmployeeHobbyTemp employeeHobbyTempNew);

    PmEmployeeHobbyTempDTO getByKey(Long id);

    List<PmEmployeeHobbyTempDTO> listAll(PmEmployeeHobbyTempQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeHobbyTempQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeHobbyTempDTO> employeeHobbyTempDTOS, HttpServletResponse response) throws IOException;

    void check(PmEmployeeHobbyTemp employeeHobbyTemp);
}
