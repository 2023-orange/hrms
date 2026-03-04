package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdPlanEmployee;
import com.sunten.hrms.td.dto.TdPlanEmployeeDTO;
import com.sunten.hrms.td.dto.TdPlanEmployeeQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 参训人员表（包括讲师） 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-05-25
 */
public interface TdPlanEmployeeService extends IService<TdPlanEmployee> {

    TdPlanEmployeeDTO insert(TdPlanEmployee planEmployeeNew);

    void delete(Long id);

    void delete(TdPlanEmployee planEmployee);

    void update(TdPlanEmployee planEmployeeNew);

    TdPlanEmployeeDTO getByKey(Long id);

    List<TdPlanEmployeeDTO> listAll(TdPlanEmployeeQueryCriteria criteria);

    Map<String, Object> listAll(TdPlanEmployeeQueryCriteria criteria, Pageable pageable);

    void download(List<TdPlanEmployeeDTO> planEmployeeDTOS, HttpServletResponse response) throws IOException;

    void deleteByEnabled(TdPlanEmployee planEmployee);
}
