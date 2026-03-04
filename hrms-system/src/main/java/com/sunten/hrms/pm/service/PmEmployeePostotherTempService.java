package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeePostotherTemp;
import com.sunten.hrms.pm.dto.PmEmployeePostotherTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeePostotherTempQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 工作外职务临时表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
public interface PmEmployeePostotherTempService extends IService<PmEmployeePostotherTemp> {

    PmEmployeePostotherTempDTO insert(PmEmployeePostotherTemp employeePostotherTempNew);

    void delete(Long id);

    void delete(PmEmployeePostotherTemp employeePostotherTemp);

    void update(PmEmployeePostotherTemp employeePostotherTempNew);

    PmEmployeePostotherTempDTO getByKey(Long id);

    List<PmEmployeePostotherTempDTO> listAll(PmEmployeePostotherTempQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeePostotherTempQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeePostotherTempDTO> employeePostotherTempDTOS, HttpServletResponse response) throws IOException;

    void check(PmEmployeePostotherTemp employeePostotherTemp);
}
