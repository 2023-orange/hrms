package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeTitleTemp;
import com.sunten.hrms.pm.dto.PmEmployeeTitleTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeTitleTempQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 职称情况临时表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeTitleTempService extends IService<PmEmployeeTitleTemp> {

    PmEmployeeTitleTempDTO insert(PmEmployeeTitleTemp employeeTitleTempNew);

    void delete(Long id);

    void delete(PmEmployeeTitleTemp employeeTitleTemp);

    void update(PmEmployeeTitleTemp employeeTitleTempNew);

    PmEmployeeTitleTempDTO getByKey(Long id);

    List<PmEmployeeTitleTempDTO> listAll(PmEmployeeTitleTempQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeTitleTempQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeTitleTempDTO> employeeTitleTempDTOS, HttpServletResponse response) throws IOException;

    void check(PmEmployeeTitleTemp employeeTitleTemp);
}
