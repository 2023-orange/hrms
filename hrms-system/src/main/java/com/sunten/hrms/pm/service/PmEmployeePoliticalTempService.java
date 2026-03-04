package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeePoliticalTemp;
import com.sunten.hrms.pm.dto.PmEmployeePoliticalTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeePoliticalTempQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 员工政治面貌临时表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
public interface PmEmployeePoliticalTempService extends IService<PmEmployeePoliticalTemp> {

    PmEmployeePoliticalTempDTO insert(PmEmployeePoliticalTemp employeePoliticalTempNew);

    void delete(Long id);

    void delete(PmEmployeePoliticalTemp employeePoliticalTemp);

    void update(PmEmployeePoliticalTemp employeePoliticalTempNew);

    PmEmployeePoliticalTempDTO getByKey(Long id);

    List<PmEmployeePoliticalTempDTO> listAll(PmEmployeePoliticalTempQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeePoliticalTempQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeePoliticalTempDTO> employeePoliticalTempDTOS, HttpServletResponse response) throws IOException;

    void check(PmEmployeePoliticalTemp employeePoliticalTemp);
}
