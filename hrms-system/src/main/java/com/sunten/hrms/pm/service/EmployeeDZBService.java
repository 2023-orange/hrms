package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.EmployeeDZB;
import com.sunten.hrms.pm.dto.EmployeeDZBDTO;
import com.sunten.hrms.pm.dto.EmployeeDZBQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-09-09
 */
public interface EmployeeDZBService extends IService<EmployeeDZB> {

    EmployeeDZBDTO insert(EmployeeDZB employeeDZBNew);

    void delete(Integer id);

    void delete(EmployeeDZB employeeDZB);

    void update(EmployeeDZB employeeDZBNew);

    EmployeeDZBDTO getByKey(Integer id);

    List<EmployeeDZBDTO> listAll(EmployeeDZBQueryCriteria criteria);

    Map<String, Object> listAll(EmployeeDZBQueryCriteria criteria, Pageable pageable);

    void download(List<EmployeeDZBDTO> employeeDZBDTOS, HttpServletResponse response) throws IOException;
}
