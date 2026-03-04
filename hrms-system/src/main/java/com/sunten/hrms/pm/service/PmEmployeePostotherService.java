package com.sunten.hrms.pm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.pm.domain.PmEmployeePostother;
import com.sunten.hrms.pm.dto.PmEmployeePostotherDTO;
import com.sunten.hrms.pm.dto.PmEmployeePostotherQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * 工作外职务表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeePostotherService extends IService<PmEmployeePostother> {

    PmEmployeePostotherDTO insert(PmEmployeePostother employeePostotherNew);

    void delete(Long id);

    void delete(PmEmployeePostother employeePostother);

    void update(PmEmployeePostother employeePostotherNew);

    PmEmployeePostotherDTO getByKey(Long id);

    List<PmEmployeePostotherDTO> listAll(PmEmployeePostotherQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeePostotherQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeePostotherDTO> employeePostotherDTOS, HttpServletResponse response) throws IOException;

    List<PmEmployeePostotherDTO> batchInsert(List<PmEmployeePostother> pmEmployeePostothers,Long employeeId);

    List<PmEmployeePostotherDTO> listAllByCheck(Long employeeId);
}
