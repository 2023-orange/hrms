package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcRegime;
import com.sunten.hrms.ac.dto.AcRegimeDTO;
import com.sunten.hrms.ac.dto.AcRegimeQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 考勤制度表 服务类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
public interface AcRegimeService extends IService<AcRegime> {

    AcRegimeDTO insert(AcRegime regimeNew);

    void delete(Long id);

    void delete(AcRegime regime);

    void update(AcRegime regimeNew);

    AcRegimeDTO getByKey(Long id);

    List<AcRegimeDTO> listAll(AcRegimeQueryCriteria criteria);

    Map<String, Object> listAll(AcRegimeQueryCriteria criteria, Pageable pageable);

    void download(List<AcRegimeDTO> regimeDTOS, HttpServletResponse response) throws IOException;
}
