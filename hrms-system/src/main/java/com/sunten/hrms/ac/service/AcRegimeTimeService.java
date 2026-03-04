package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcRegimeTime;
import com.sunten.hrms.ac.dto.AcRegimeTimeDTO;
import com.sunten.hrms.ac.dto.AcRegimeTimeQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 考勤制度时间段表 服务类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
public interface AcRegimeTimeService extends IService<AcRegimeTime> {

    AcRegimeTimeDTO insert(AcRegimeTime regimeTimeNew);

    void delete(Long id);

    void delete(AcRegimeTime regimeTime);

    void update(AcRegimeTime regimeTimeNew);

    AcRegimeTimeDTO getByKey(Long id);

    List<AcRegimeTimeDTO> listAll(AcRegimeTimeQueryCriteria criteria);

    Map<String, Object> listAll(AcRegimeTimeQueryCriteria criteria, Pageable pageable);

    void download(List<AcRegimeTimeDTO> regimeTimeDTOS, HttpServletResponse response) throws IOException;
}
