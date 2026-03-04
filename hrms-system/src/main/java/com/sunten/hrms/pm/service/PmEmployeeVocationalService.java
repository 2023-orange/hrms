package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeVocational;
import com.sunten.hrms.pm.dto.PmEmployeeVocationalDTO;
import com.sunten.hrms.pm.dto.PmEmployeeVocationalQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 职业资格表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeVocationalService extends IService<PmEmployeeVocational> {

    PmEmployeeVocationalDTO insert(PmEmployeeVocational employeeVocationalNew);

    void delete(Long id);

    void delete(PmEmployeeVocational employeeVocational);

    void update(PmEmployeeVocational employeeVocationalNew);

    PmEmployeeVocationalDTO getByKey(Long id);

    List<PmEmployeeVocationalDTO> listAll(PmEmployeeVocationalQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeVocationalQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeVocationalDTO> employeeVocationalDTOS, HttpServletResponse response) throws IOException;

    List<PmEmployeeVocationalDTO> listAllByCheck(Long employeeId);

    List<PmEmployeeVocationalDTO> batchInsert(List<PmEmployeeVocational> employeeVocationals,Long employeeId);
}
