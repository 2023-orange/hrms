package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReEmpMesMonthly;
import com.sunten.hrms.re.dto.ReEmpMesMonthlyDTO;
import com.sunten.hrms.re.dto.ReEmpMesMonthlyQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 每月人员情况存档 服务类
 * </p>
 *
 * @author liangjw
 * @since 2022-01-07
 */
public interface ReEmpMesMonthlyService extends IService<ReEmpMesMonthly> {

    ReEmpMesMonthlyDTO insert(ReEmpMesMonthly empMesMonthlyNew);

    void delete(Long id);

    void delete(ReEmpMesMonthly empMesMonthly);

    void update(ReEmpMesMonthly empMesMonthlyNew);

    ReEmpMesMonthlyDTO getByKey(Long id);

    List<ReEmpMesMonthlyDTO> listAll(ReEmpMesMonthlyQueryCriteria criteria);

    Map<String, Object> listAll(ReEmpMesMonthlyQueryCriteria criteria, Pageable pageable);

    void download(List<ReEmpMesMonthlyDTO> empMesMonthlyDTOS, HttpServletResponse response) throws IOException;

    void autoInsertEmpMesMonthlyForReDemand();
}
