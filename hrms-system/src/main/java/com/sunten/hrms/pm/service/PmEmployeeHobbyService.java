package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeHobby;
import com.sunten.hrms.pm.dto.PmEmployeeHobbyDTO;
import com.sunten.hrms.pm.dto.PmEmployeeHobbyQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 技术爱好表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeHobbyService extends IService<PmEmployeeHobby> {

    PmEmployeeHobbyDTO insert(PmEmployeeHobby employeeHobbyNew);

    void delete(Long id);

    void delete(PmEmployeeHobby employeeHobby);

    void update(PmEmployeeHobby employeeHobbyNew);

    PmEmployeeHobbyDTO getByKey(Long id);

    List<PmEmployeeHobbyDTO> listAll(PmEmployeeHobbyQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeHobbyQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeHobbyDTO> employeeHobbyDTOS, HttpServletResponse response) throws IOException;

    List<PmEmployeeHobbyDTO> batchInsert(List<PmEmployeeHobby> pmEmployeeHobbies,Long employeeId);

    Map<String, Object> listAllSummary(PmEmployeeHobbyQueryCriteria criteria);

    List<PmEmployeeHobbyDTO> listAllByCheck(Long employeeId);
}
