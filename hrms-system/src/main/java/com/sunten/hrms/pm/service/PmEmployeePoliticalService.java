package com.sunten.hrms.pm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.pm.domain.PmEmployeePolitical;
import com.sunten.hrms.pm.dto.PmEmployeePoliticalDTO;
import com.sunten.hrms.pm.dto.PmEmployeePoliticalQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * 政治面貌表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeePoliticalService extends IService<PmEmployeePolitical> {

    PmEmployeePoliticalDTO insert(PmEmployeePolitical employeePoliticalNew);

    void delete(Long id);

    void delete(PmEmployeePolitical employeePolitical);

    void update(PmEmployeePolitical employeePoliticalNew);

    PmEmployeePoliticalDTO getByKey(Long id);

    List<PmEmployeePoliticalDTO> listAll(PmEmployeePoliticalQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeePoliticalQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeePoliticalDTO> employeePoliticalDTOS, HttpServletResponse response) throws IOException;

    List<PmEmployeePoliticalDTO> batchInsert(List<PmEmployeePolitical> pmEmployeePoliticals,Long employeeId);

    List<PmEmployeePoliticalDTO> listAllByCheck(Long employeeId);
}
