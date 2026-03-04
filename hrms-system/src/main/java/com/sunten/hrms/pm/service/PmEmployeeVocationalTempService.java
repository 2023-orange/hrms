package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeVocationalTemp;
import com.sunten.hrms.pm.dto.PmEmployeeVocationalTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeVocationalTempQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 职业资格临时表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeVocationalTempService extends IService<PmEmployeeVocationalTemp> {

    PmEmployeeVocationalTempDTO insert(PmEmployeeVocationalTemp employeeVocationalTempNew);

    void delete(Long id);

    void delete(PmEmployeeVocationalTemp employeeVocationalTemp);

    void update(PmEmployeeVocationalTemp employeeVocationalTempNew);

    PmEmployeeVocationalTempDTO getByKey(Long id);

    List<PmEmployeeVocationalTempDTO> listAll(PmEmployeeVocationalTempQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeVocationalTempQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeVocationalTempDTO> employeeVocationalTempDTOS, HttpServletResponse response) throws IOException;

    void check(PmEmployeeVocationalTemp employeeVocationalTempNew);
}
