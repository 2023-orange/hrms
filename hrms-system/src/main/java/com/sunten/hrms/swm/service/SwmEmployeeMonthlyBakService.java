package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmEmployeeMonthlyBak;
import com.sunten.hrms.swm.dto.SwmEmployeeMonthlyBakDTO;
import com.sunten.hrms.swm.dto.SwmEmployeeMonthlyBakQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 薪酬员工信息每月备份表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-09-15
 */
public interface SwmEmployeeMonthlyBakService extends IService<SwmEmployeeMonthlyBak> {

    SwmEmployeeMonthlyBakDTO insert(SwmEmployeeMonthlyBak employeeMonthlyBakNew);

    void delete(Long id);

    void delete(SwmEmployeeMonthlyBak employeeMonthlyBak);

    void update(SwmEmployeeMonthlyBak employeeMonthlyBakNew);

    SwmEmployeeMonthlyBakDTO getByKey(Long id);

    List<SwmEmployeeMonthlyBakDTO> listAll(SwmEmployeeMonthlyBakQueryCriteria criteria);

    Map<String, Object> listAll(SwmEmployeeMonthlyBakQueryCriteria criteria, Pageable pageable);

    void download(List<SwmEmployeeMonthlyBakDTO> employeeMonthlyBakDTOS, HttpServletResponse response) throws IOException;

    void autoInsertBySwmEmployeeMonthly(String period);
}
